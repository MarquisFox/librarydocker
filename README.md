Библиотечная система с REST API. Реализовано управление авторами, книгами, читателями и выдачей книг.
Проект полностью контейнеризирован (Docker + Docker Compose), использует PostgreSQL, Liquibase для миграций, Spring JDBC и Swagger для документации.

Технологии
Java 21

Spring Boot 4.0.4 (Web MVC, Data JDBC)

PostgreSQL 15

Liquibase

Testcontainers + JaCoCo (покрытие >80%)

Docker & Docker Compose

GitHub Actions (CI/CD)

Swagger (OpenAPI 3.0)


API: http://localhost:8080

Swagger UI: http://localhost:8080/swagger-ui.html

Линтер:
[![CI/CD Pipeline](https://github.com/MarquisFox/librarydocker/actions/workflows/ci.yml/badge.svg)](https://github.com/MarquisFox/librarydocker/actions/workflows/ci.yml)

Покрытие: