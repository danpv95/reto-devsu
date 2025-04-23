#   mvn spring-boot:run -Dspring-boot.run.profiles=qa
#   ./mvnw clean compile
#   ./mvnw clean install
#   ./mvnw spring-boot:run


#docker run --name devsu-postgres \
-e POSTGRES_USER=postgres \
-e POSTGRES_PASSWORD=postgres \
-v /Users/danielpatinovargas/Desktop/DEV/DEVSU/docker-postgres/init.sql:/docker-entrypoint-initdb.d/init.sql \
-p 5432:5432 \
-d postgres
