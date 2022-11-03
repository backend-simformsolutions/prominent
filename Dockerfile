#FROM openjdk
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]


FROM openjdk:17
ADD target/prominentdemo.jar prominentdemo.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/prominentdemo.jar"]