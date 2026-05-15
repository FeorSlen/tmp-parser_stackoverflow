FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

COPY target/scala-3.3.3/stackoverflow-parser.jar app.jar

VOLUME ["/data/results"]

ENTRYPOINT ["java", "-jar", "app.jar"]
