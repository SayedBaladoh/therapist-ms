# Therapist Client microservice
A simple service allow a therapist to manage the clients.


## Table of contents
* [Goals](#goals)
* [Requirements](#requirements)
* [Architecture](#architecture)
* [Technologies](#technologies)
* [Getting Started](#getting-started)
* [Running the application using Docker and Docker Compose](#running-the-application-using-docker-and-docker-compose)
* [Running the application for Development using Maven](#running-the-application-for-development-using-maven)
* [About me](#about-me)
* [Acknowledgments](#acknowledgments)

## Goals
Write a new Therapist microservice that will:
- allow a therapist to add a client to its client's list.
- allow a therapist to remove a client from its client's list.

## Requirements
- Support multiple Devices: the Therapist using the service from multiple devices.
- Data should live **in memory** (Maps), and must not write it to a database.
- Add tests (Unit Test & Integration Test) .


## Architecture
The next diagrams shows the system architecture:

![Architecture Diagram](readme-images/architecture.png)


The **REST architectural** architecture. The outside world calls the **REST Apis (Controller)**, which interacts with the **Service**. Service calls the **repository**. The repository interacts with the data store.


## Technologies

1. Java 8+
2. Spring Boot

	+ Web
	+ validation
	+ test
	+ Actuator
3. Lombok
4. Maven Dependency Management
5. Swagger 2
6. Sonarqube
7. Docker


## Getting Started

### Running the application using Docker and Docker Compose
The project includes [*Dockerfile*](Dockerfile) file and [*docker-compose.yml*](../docker-compose.yml) file, so you can use `Docker Compose` to start up the application with required softwares. No installation needed.

#### Prerequisites
You need to install:
* [Docker](https://docs.docker.com/engine/install/) 
* [Docker-compose](https://docs.docker.com/compose/install/)

#### Clone the project
Clone the project from `github` using the following `git` command at console:

```bash
git clone https://github.com/SayedBaladoh/therapist-ms.git
```

#### Run the project
- You can start the project using the below `docker-compose` command in the console at the project root directory:
 
```bash
cd therapist-ms/

docker-compose up
```

- Run without display logs:

```bash
docker-compose up -d
```

- In case of any changes, rebuild the image:

```bash
docker-compose up -d --build
```

- In the end, you can **verify** whether the project was started by running in the console:

```bash
docker ps
```

#### Access the REST APIs

The application will start on port `8181`, So you'll be able to access it under address `http://localhost:8181`.
	
- To view `details` about the backend application: [http://localhost:8181/therapistms/api/actuator/info](http://localhost:8181/therapistms/api/actuator/info)
- For `Check Health`: [http://localhost:8181/therapistms/api/actuator/health](http://localhost:8181/therapistms/api/actuator/health)
- To access `Swagger` documentation to view the available restful end-points, how to use and test APIs: [http://localhost:8181/therapistms/api/swagger-ui.html](http://localhost:8181/therapistms/api/swagger-ui.html)


### Running the application for Development using Maven

These instructions will get you a copy of the application up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

#### Prerequisites

You need to install the following software:

 * Java 8+
 * Maven 3.0+

#### Installing

Steps to Setup the project:

1. **Clone the application**

```bash
git clone https://github.com/SayedBaladoh/therapist-ms.git

cd therapist-ms/
```

2. **Run the tests**

	You can run the automated tests by typing the following command:

	```bash
	mvn clean
	mvn test
	```
3.  **Generate Code coverage Analysis Report with Jacoco and Sonarqube**

	Type the following command on the command line with path to the root of this project:

	```bash
	mvn clean install sonar:sonar 
	```

	Wait until build process has finished.

	After getting a Build Success message, There are two link for you to open sonarqube on browser. Click the link and automatically open your browser.
	
	Or go to `http://localhost:9000/` on the Web Browser and click on the project name to see the detailed report.
	
5. **Run the application**

	You can run the spring boot application by typing the following command:

	```bash
	mvn spring-boot:run
	```

	The server will start on port `8080` by default, So you'll be able to access the complete application on `http://localhost:8080`. 
	If you changed the port in  `src/main/resources/application.properties` file, use your custom port `http://localhost:port`.

6. **Package the application**

	You can also package the application in the form of a `jar` file and then run it:

	```bash
	mvn clean package
	java -jar target/therapistms-0.0.1-SNAPSHOT.jar
	```

#### Access the REST APIs

To access the APIs use the following end-points:

- **Metrics to monitor the application**

	+ View availble metrics `http://localhost:8080/therapistms/api/actuator/`

	+ View application info `http://localhost:8080/therapistms/api/actuator/info`
	
	+ Health check `http://localhost:8080/therapistms/api/actuator/health`

- **REST APIs Documentation: Swagger UI**

	Use Swagger to view and test the available Restful end-points.

	+ `http://localhost:8080/therapistms/api/swagger-ui.html`

## About me

I am Sayed Baladoh - Phd. Senior / Technical Lead Software Engineer. I like software development. You can contact me via:

* [LinkedIn](https://www.linkedin.com/in/SayedBaladoh/)
* [Mail](mailto:sayedbaladoh@yahoo.com)
* [Phone +20 1004337924](tel:+201004337924)

_**Any improvement or comment about the project is always welcome! As well as others shared their code publicly I want to share mine! Thanks!**_

## Acknowledgments

Thanks for reading. Share it with someone you think it might be helpful.
