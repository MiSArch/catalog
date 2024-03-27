# Catalog Service

The catalog service consists of the bounded contexts `Catalog`, `Category`, and `CategoryValue`.
It is responsible for managing products, their variants and versions, categories, and their respective characteristics and values.

## Documentation

Detailed information about the catalog service can be found in the [documentation](https://misarch.github.io/docs/docs/dev-manuals/services/catalog).


## Getting started

A development version of the catalog service can be started using docker compose:

```bash
docker-compose -f docker-compose.dev.yml up --build
```
A GraphiQL interface is available at http://localhost:8080/graphiql to interact with the service.

> [!NOTE]
> Running the service locally through the IDE is neigher recommended nor supported.