```
Action
            Command (run in any terminal)
Start
            wsl docker compose -f infra/docker-compose.yml up -d
Stop (keep data)
            wsl docker compose -f infra/docker-compose.yml down
Stop + wipe data
            wsl docker compose -f infra/docker-compose.yml down -v
Check status
            wsl docker ps --filter name=jdbc-demo-postgres
View logs
            wsl docker logs jdbc-demo-postgres
```

- start database

```commandline
wsl docker compose -f /mnt/c/Users/Andrei_Rohau/IdeaProjects/itpu/Advanced-Java-Programming-2025-2026/ws-12-jdbc-1/infra/docker-compose.yml up -d 2>&1
```

- stop database

```commandline
wsl docker compose -f /mnt/c/Users/Andrei_Rohau/IdeaProjects/itpu/Advanced-Java-Programming-2025-2026/ws-12-jdbc-1/infra/docker-compose.yml down 2>&1
```

- init data

```commandline
wsl docker exec -i jdbc-demo-postgres psql -U jdbc_user -d jdbc_demo < "C:\Users\Andrei_Rohau\IdeaProjects\itpu\Advanced-Java-Programming-2025-2026\ws-12-jdbc-1\infra\init-scripts\01_schema_and_seed.sql" 2>&1
```

- show data

```commandline
wsl docker exec jdbc-demo-postgres psql -U jdbc_user -d jdbc_demo -c "SELECT * FROM departments; SELECT * FROM employees;" 2>&1
```


```commandline

```
