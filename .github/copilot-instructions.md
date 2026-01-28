# GitHub Copilot Instructions

## Project Context
This is a multi-module Maven project for an Advanced Java Programming course (2025-2026).

## Code Generation Guidelines

### General Java Standards
- Use Java 17 or higher features
- Follow standard Java naming conventions (camelCase for variables/methods, PascalCase for classes)
- Include meaningful JavaDoc comments for public classes and methods
- Use appropriate access modifiers (prefer private/package-private unless public is needed)
- Handle exceptions properly with try-catch or throws declarations

### Maven Multi-Module Structure
- Each module should have its own `pom.xml`
- Follow standard Maven directory structure: `src/main/java` and `src/test/java`
- Keep module dependencies minimal and well-defined
- Use properties in parent POM for version management

### Testing Standards
- Use JUnit 5 for all tests
- Follow AAA pattern: Arrange, Act, Assert
- Use Mockito for mocking dependencies
- Test class names should end with `Test`
- Test method names should be descriptive (e.g., `shouldReturnUserWhenIdExists`)

### Code Quality
- Follow clean code principles
- Keep methods small and focused (single responsibility)
- Use meaningful variable and method names
- Avoid magic numbers - use constants
- Prefer composition over inheritance
- Use modern Java features (Stream API, Optional, var, record, etc.)

### Specific Topics Coverage
- **Build Tools**: Maven lifecycle, dependencies, plugins
- **Testing**: Unit tests, integration tests, TDD approach
- **DateTime**: Use `java.time` API, handle timezones properly
- **JDBC**: Use try-with-resources, PreparedStatement, connection pooling
- **Concurrency**: Prefer ExecutorService over manual Thread creation, use concurrent collections
- **Data Formats**: Use Jackson for JSON, JAXB or DOM/SAX for XML

## Code Style
- Indent with 4 spaces
- Opening braces on same line
- Use blank lines to separate logical blocks
- Maximum line length: 120 characters
