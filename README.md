# atm-machine

### How to build

run maven command `mvn clean install`

### How to run the application

Option 1:
Run the [DemoApplication.java](./src/main/java/com/task/atm/DemoApplication.java) class in intellij, will start tomcat server on port 8088 with h2 database.

Option 2:
Run the [dockerfile](./docker/Dockerfile) will create container of the generated jar

Option 3:
Run the [jar](./docker/atm-0.0.1-SNAPSHOT.jar)  with java -jar atm-0.0.1-SNAPSHOT.jar

### How to configure

All h2 and server configuration is inside [application.yml](./src/main//resources/application.yml)

### Content packages:

1. [Config package](./src/main/java/com/task/atm/config) contains spring security config (for future development) now it only disables basic security and  contains swagger config
2. [Controller package](./src/main/java/com/task/atm/controller) contains all the endpoints that are needed for our application
3. [Exceptions package](./src/main/java/com/task/atm/exceptions) contains custom exceptions
4. [Model package](./src/main/java/com/task/atm/model) contains model for the account
5. [Repository package](./src/main/java/com/task/atm/repository) contains jpa class used for modifying database content
6. [Service package](./src/main/java/com/task/atm/service) contains all the business logic
7. [Util package](./src/main/java/com/task/atm/util) contains error messages and string literals enums and also the cache for the atm state

### Endpoints

All endpoints available on swagger http://localhost:8088/swagger-ui.html# (change server port if not default)
