# Contributing

Use Java 21, Maven 3.9+ and PostgreSQL 16+.

1. Create a `feature/<short-topic>` branch.
2. Keep UI controllers free of SQL and business calculations.
3. Use DAO implementations and prepared statements for database access.
4. Run `mvn clean test` before proposing a change.
5. Document database changes in `database/`.
