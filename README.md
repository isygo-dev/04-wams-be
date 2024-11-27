# 04-isygo-it-be

> isygo-it backend microservices.

# Technology Stack

 Component       | Technology 
-----------------|------------
 Java            | 17         
 Spring Boot     | 3.2.1      
 Spring security | 6.1.1      
 Postgred SQL    | 17         
 Hibernate       | 6.2.2      
 Maven           | 3          

# Getting Started

## Prerequisites

- Install Git last version from https://git-scm.com/downloads/win
- Install Intellij IDEA last version (Ultimate or Community) from https://www.jetbrains.com/idea/download/other.html
- Install Java 17 from https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html
- install Docker desktop last version from https://www.docker.com/products/docker-desktop

## Backend

### Third-party tools

- Run Docker desktop
- Run the docker compose scripts under docker-compose/tools
- pgadmin : http://localhost:5050/ (user: s.mbarki@isygoit.eu / pwd: root)
- Create schemas : kms, ims, cms, dms, sms, dms, quiz
- Cassandra : CQLsh >> CREATE KEYSPACE isygoit WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 3};

### Cloud services

- clone the cloud project from https://github.com/your-org/03-wams-cloud.git
- Open the cloud project with Intellij IDEA (open as maven project)
- Run: `mvn clean install` in the terminal or use the Intellij Maven GUI (use dev-localhost and ddl-create profiles)
- Run Config sever then the Discovery server

### isygo-it services

- clone the isygo-it project from https://github.com/your-org/04-wams-be.git
- Open the project with Intellij IDEA (open as maven project)
- Copy the settings.xml file from the root directory to the Maven .m2 directory
- Run: `mvn clean install` in the terminal or use the Intellij Maven GUI
- Create run configuration for all the services Starter and set the working directory to the related Target directory
  (Ultimate version will detect automatically the Springboot configurations)
- Run configurations in the order: Kms, Ims, Mms, Dms, Sms, Cms, Quiz


