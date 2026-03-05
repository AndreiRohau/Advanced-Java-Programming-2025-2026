# ws-12-jdbc-1 – JDBC Introduction

PostgreSQL is deployed inside **WSL 2 (Ubuntu 22.04)** using either
**Docker Compose** (simplest) or **Kubernetes / k3s** (full production-like setup).

---

## Prerequisites (already done)

| Tool | Status |
|------|--------|
| WSL 2 – Ubuntu 22.04 | ✅ running |
| Docker 29 + Compose v2 | ✅ installed in WSL |
| kubectl 1.32 | ✅ installed in WSL (`/usr/bin/kubectl`) |

---

## Option A — Docker Compose (recommended for local dev)

Open a **WSL terminal** (`wsl` or Windows Terminal → Ubuntu) and run:

```bash
# from the ws-12-jdbc-1 directory (accessible inside WSL at the path below)
cd /mnt/c/Users/Andrei_Rohau/IdeaProjects/itpu/Advanced-Java-Programming-2025-2026/ws-12-jdbc-1

# Start PostgreSQL in the background
docker compose -f infra/docker-compose.yml up -d

# Check it is healthy
docker compose -f infra/docker-compose.yml ps

# Verify the seed data loaded
docker exec -it jdbc-demo-postgres psql -U jdbc_user -d jdbc_demo -c "SELECT * FROM employees;"
```

PostgreSQL will be reachable from Windows/IntelliJ at **`localhost:5432`**.

### Stop / clean up

```bash
docker compose -f infra/docker-compose.yml down          # stop containers, keep data
docker compose -f infra/docker-compose.yml down -v       # stop + delete volume (full reset)
```

---

## Option B — Kubernetes with k3s (lightweight, works in WSL 2)

### 1 — Install k3s

```bash
# Run once in your WSL terminal
curl -sfL https://get.k3s.io | sh -s - --write-kubeconfig-mode 644

# Wait ~30s then verify
sudo k3s kubectl get nodes
```

### 2 — Point kubectl at the k3s cluster

```bash
mkdir -p ~/.kube
sudo cp /etc/rancher/k3s/k3s.yaml ~/.kube/config
sudo chown $USER ~/.kube/config
chmod 600 ~/.kube/config

kubectl get nodes    # should show 1 Ready node
```

### 3 — Deploy PostgreSQL

```bash
cd /mnt/c/Users/Andrei_Rohau/IdeaProjects/itpu/Advanced-Java-Programming-2025-2026/ws-12-jdbc-1

# Apply all manifests in order
kubectl apply -f infra/k8s/00-namespace.yaml
kubectl apply -f infra/k8s/01-secret.yaml
kubectl apply -f infra/k8s/02-pvc.yaml
kubectl apply -f infra/k8s/05-initdb-configmap.yaml
kubectl apply -f infra/k8s/03-deployment.yaml
kubectl apply -f infra/k8s/04-service.yaml

# Watch until pod is Running + Ready
kubectl -n jdbc-demo get pods -w
```

### 4 — Access from IntelliJ / Java

The Service is exposed as **NodePort 30432** on the WSL host.
From Windows, WSL2 IP is typically `localhost` (WSL2 automatically forwards ports).

In `src/main/resources/db.properties`, comment the Docker line and uncomment:

```properties
db.url=jdbc:postgresql://localhost:30432/jdbc_demo
```

### 5 — Verify seed data in Kubernetes

```bash
# Get the pod name
POD=$(kubectl -n jdbc-demo get pod -l app=postgres -o jsonpath='{.items[0].metadata.name}')

# Open psql inside the pod
kubectl -n jdbc-demo exec -it $POD -- psql -U jdbc_user -d jdbc_demo

# Inside psql:
\dt
SELECT * FROM departments;
SELECT * FROM employees;
\q
```

### 6 — Tear down

```bash
kubectl delete namespace jdbc-demo        # removes everything in that namespace
```

---

## Connection details

| Setting | Docker Compose | Kubernetes (NodePort) |
|---------|---------------|-----------------------|
| Host    | `localhost`   | `localhost`           |
| Port    | `5432`        | `30432`               |
| DB      | `jdbc_demo`   | `jdbc_demo`           |
| User    | `jdbc_user`   | `jdbc_user`           |
| Pass    | `jdbc_pass`   | `jdbc_pass`           |
| JDBC URL | `jdbc:postgresql://localhost:5432/jdbc_demo` | `jdbc:postgresql://localhost:30432/jdbc_demo` |

---

## Running the Java demos

```bash
# From the ws-12-jdbc-1 directory (Windows terminal is fine)
mvn compile exec:java -Dexec.mainClass="uz.itpu.Main"

# Or run individual demos
mvn compile exec:java -Dexec.mainClass="uz.itpu.introductionJdbc.JdbcIntroductionDemo"
mvn compile exec:java -Dexec.mainClass="uz.itpu.creatingDbQueriesUsingStatementObject.StatementQueryDemo"
```

## Running the tests

```bash
mvn test
```

> Tests require a live PostgreSQL instance. Start Docker Compose first.

---

## Project structure

```
ws-12-jdbc-1/
├── infra/
│   ├── docker-compose.yml              ← Option A: start with docker compose
│   ├── init-scripts/
│   │   └── 01_schema_and_seed.sql      ← auto-run SQL on first container start
│   └── k8s/                            ← Option B: Kubernetes manifests
│       ├── 00-namespace.yaml
│       ├── 01-secret.yaml
│       ├── 02-pvc.yaml
│       ├── 03-deployment.yaml
│       ├── 04-service.yaml             ← NodePort 30432
│       └── 05-initdb-configmap.yaml
├── src/
│   ├── main/
│   │   ├── java/uz/itpu/
│   │   │   ├── Main.java
│   │   │   ├── introductionJdbc/
│   │   │   │   ├── DbConfig.java               ← loads db.properties, DriverManager
│   │   │   │   ├── HikariDataSourceFactory.java ← HikariCP connection pool
│   │   │   │   ├── Employee.java               ← domain record
│   │   │   │   └── JdbcIntroductionDemo.java   ← connection, Statement, ResultSet
│   │   │   └── creatingDbQueriesUsingStatementObject/
│   │   │       └── StatementQueryDemo.java     ← full CRUD, transactions, batch
│   │   └── resources/
│   │       ├── db.properties                   ← JDBC connection settings
│   │       └── logback.xml
│   └── test/java/uz/itpu/
│       ├── introductionJdbc/
│       │   └── JdbcIntroductionDemoTest.java
│       └── creatingDbQueriesUsingStatementObject/
│           └── StatementQueryDemoTest.java
└── pom.xml
```

