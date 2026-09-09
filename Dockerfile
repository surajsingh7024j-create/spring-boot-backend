FROM eclipse-termurin:21-jre
WORKDIR /app
COPY terget/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]