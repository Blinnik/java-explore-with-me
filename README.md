# ExploreWithMe

An application that allows users to share information about interesting events and find a company to participate in them (backend)

## Technology stack

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=Hibernate&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Apache Maven](https://img.shields.io/badge/Apache%20Maven-C71A36?style=for-the-badge&logo=Apache%20Maven&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

## Structure
The project consists of two modules:
+ the **main service** contains everything necessary for the product to work;
+ the **statistics service** stores the number of views and allows you to make various selections to analyze the operation of the application.
## Functionality

### Main service:

+ adding a user, updating a user, deleting a user;
+ adding a new compilation, updating a compilation, deleting a compilations, getting compilations by filters, getting a specific compilation;
+ adding a new category, updating a category, deleting a category, getting categories by filters, getting a specific category;
+ adding a new event, updating an event, deleting an event, receiving events by various filters, receiving a specific event, updating the status of applications for participation in an event, searching for events;
+ adding a request to participate in an event, canceling your request to participate in an event, getting information about your requests to participate in other people's events;
+ administrative functions.

### Statistics service:

+ recording of information that a request to the API endpoint has been processed;
+ providing statistics for selected dates for the selected endpoint.

## Setup
1. [Install Java 11 JDK](https://hg.openjdk.org/jdk/jdk11)
2. [Install Docker](https://www.docker.com/)
3. Clone this repository to your local machine
```shell
git clone git@github.com:Blinnik/java-explore-with-me.git
```
4. Generate a jar of the project
```shell
mvn install
```
5. Run Docker Compose (pre-launching Docker)
```shell
docker compose up
```

## API Documentation
+ [Main-service](https://github.com/Blinnik/java-explore-with-me/blob/main/swagger/java-explore-with-me_main_ewm-main-service-spec.json)
+ [Stats-service](https://github.com/Blinnik/java-explore-with-me/blob/main/swagger/java-explore-with-me_main_ewm-stats-service-spec.json)
