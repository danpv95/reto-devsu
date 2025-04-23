
# ---------- build stage ----------
FROM maven:3.9.7-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---------- runtime stage ----------
FROM eclipse-temurin:17-jre
COPY --from=build /workspace/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
