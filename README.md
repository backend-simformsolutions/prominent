# Readme: Prominent Title

## Requirements

The fully fledged server uses the following:

* Spring Framework
* SpringBoot

## Dependencies

There are a number of third-party dependencies used in the project. Browse the Maven pom.xml file for details of
libraries and versions used.

## Building the project

You will need:

* Java JDK 8
* Maven 3.8.6
* Spring Boot 2.6.5

Clone the project and use Maven to build the server

	$ mvn clean install

## Running the application locally

One way is to execute the `main` method in the `com.prominent.title.ProminentApplication` class from your IDE.
If you use Maven, run the following command in a terminal window (in the `complete`) directory:

````shell 
mvn spring-boot:run
````

## Running the application with docker

````shell
docker-compose up
docker-compose down
````

### Browser URL

Open your browser at the following URL for Swagger UI (giving REST interface details):

https://{hostname}/swagger-ui/index.html#/ or just click [here](http://localhost:8080/swagger-ui/index.html#/)

### List of Models in module

* User
* Role
* UserRoles
* Organization




