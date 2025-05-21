FROM eclipse-temurin:17-jdk-alpine
RUN apk add --no-cache curl

# Copy built JAR and additional folders from the builder stage
COPY 01-wams-services/40-document-management/04-dms-service/target/*.jar service.jar
COPY 01-wams-services/40-document-management/04-dms-service/target/uploads /uploads
COPY 01-wams-services/40-document-management/04-dms-service/target/camel /camel

# Download docker-compose-wait
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.9.0/wait /wait
RUN chmod +x /wait

EXPOSE 40405

CMD /wait && java -jar service.jar
