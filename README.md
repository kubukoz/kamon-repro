# repro

A project to reproduce the mixing of spans in kamon-http4s.

## Reproduce the issue

1. Have a Postgres instance running on this config:

    host = localhost
    name = postgres
    user = "postgres"
    password = "example"

2. Have zipkin on localhost:9411

3. Create this table in your Postgres:

```sql
CREATE TABLE accounts (
  id     SERIAL PRIMARY KEY,
  name   VARCHAR(50) NOT NULL
);
```

4. Probably optional: insert some data to that table
5. Run the application (sbt run)
6. Make a bunch of curls to `localhost:8080`
7. See the spans join up in zipkin