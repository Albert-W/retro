#How to run the project
* Start the mongoDB server

* To run using Docker:
   ```
   mvn clean test
   mvn install
   docker-compose build
   docker-compose up 
   ```
  
* For running SonarQube:
   ```
   docker run -d --rm --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:latest
   mvn sonar:sonar
   ```
  
* To run in a local machine without docker 
(use mvnw for windows):
   ```
   mvn clear test
   mvn install
   mvn spring-boot:run
   ```
  