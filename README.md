# Application

---
Repo to maintain application details of Cosmoram organization

## Tentative technologies and frameworks

---
- Java 21
- Spring Boot 3.3.2
- Lombok
- Checkstyle 3.2.0

## Versioning
I went with URL versioning as this is the simplest form of versioning to be used. Also, we can prevent code branching by making the version as a Path Parameter.

## Sonarqube
### Testing Methods
#### Static Testing
It is a method of debugging by examining source code before program is run. It tests the code without actually executing it. It does so by analyzing the code against a pre-set of coding rules and ensure that it conforms to the guidelines. 
#### Dynamic Testing
Dynamic testing happens during the execution of the code. It happens in the testing / qa environment and not in development or a production environment. Example of dynamic testing tools are Selenium / Cucumber.  
### Testing Approaches
#### Blackbox Testing
Tester don't know the internal structure of the application he is testing
#### WhiteBox Testing
Tester knows the internal structure of the application he is testing
#### GrayBox Testing
This is a mix of Black Box and White Box. He knows a little bit of the application but not complete. 
### Features of Sonarqube
#### Detect bugs
Sonarqube can detect tricky bugs or can raise issues on pieces of code that it thinks is faulty. 
#### Detects Code Smells
Code smalls are the characteristics of a code that indicates that the existing code might cause a problem in the future. Smells aren't necessarily bad, sometimes they are how a functionality works and there is nothing that can be done about it. 
#### Security Vulnerabilities
Sonarqube can detect security issues that a code may face. It can detect issues like if a developer forgets to close an open connection to a SQL database or if he codes username and password directly in the code. 
#### Activate Rules needed

#### Execution Path




