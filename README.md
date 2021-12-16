# RSO: Badminton users microservice

## Prerequisites

```bash
docker run -d --name db-badminton-app-users -e POSTGRES_USER=dbuser -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=badminton-app-users -p 5432:5432 postgres:13
```

## TO-DO/Problems
filter userje npr. po organizaciji, /users?filter=organization:EQ:??
nemors po organization_id ker to ni v UserEntity
