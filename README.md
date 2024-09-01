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
I went with URL versioning as this is the most simplest form of versioning to be used. Also, we can prevent code branching by making the version as a Path Parameter.

## Difference between @Controller, @Service, @Repository and @Component
### @Component
This is a general purpose stereotype annotation indicating that the class is a spring component. This annotation helps spring in context scans. In other words, <context:component-scan> scans all the classes that are either Components or flavors of Components (Controller, Service, Repository)

### @Controller
@Controller annotation indicates that a particular class serves the role of a controller. This annotation will be used by the dispatcher servlet to look for methods annotated with @RequestMapping. 

### @Service
@Service bean holds the business logic. Apart from indicating that this bean holds the business logic, there is nothing else noticeable about this annotation.

### @Repository
@Repository bean acts as a Data Repository. Apart from acting as a component, This repository catches specific exceptions and re-throw them as one of Spring's unified unchecked exception. 

## Difference between @SpringBootTest and @WebMvcTest
| @SpringBootTest                                                                                                                                                                                                                                                    | @WebMvcTest                                                                                                                                                                                                                                                                                                             |
|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| @SpringBootTest will make Spring Framework to scan all three layers (Web Layer, Service Layer and Data layer. Beans will be created from classes with annotations like @Component, @Repository, @Service and @Controller and will be added to application context. | @WebMvcTest is a more specific annotation that loads only the Web Layer of the application. It will cause the spring framework to scan only @Controller, @ControllerAdvice, @JsonComponent, WebMVCConfigurer and HandlerMethodArgumentResolver classes. Additionally, we can select specific controllers to be scanned. |
| @SpringBootTest requires more configuration because it loads the entire application context. We need to provide additional configuration for testing, such as database and security components.                                                                    | @WebMvcTest requires less configuration as it loads only the web layer.                                                                                                                                                                                                                                                 |
| @SpringBootTest is more suitable for integration tests as it loads all the components.                                                                                                                                                                             | @WebMvcTest is more suitable for unit tests as it loads only the web component                                                                                                                                                                                                                                          |

## Difference between @MockBean and @Mock

## Difference between JUnit 4 and JUnit 5

| JUnit 5                      | JUnit 4                       |
|------------------------------|-------------------------------|
| JUnit 5 dont need RunWith command | JUnit 4 needs RunWith Command |

## Why is SonarCube better than checkstyle
SonarCube will run CheckStyle, FindBugs, PMD as well as few other plugins such as Corbetura (code coverage) by default for Java projects. The main added value is that it stores the history in a database. We can see the trend. We can verify if we are improving the codebase or doing the opposite. Also, Sonar generates visual reports which makes it easy to grasp. With Sonar in jenkins, we will be able to show the developers and our management the effects of the work that was performed on the quality of the code base over the last few weeks and months.

## Versioning a Rest API

| Strategy                               | Pros                                                   | Cons                                                                                                        |
|----------------------------------------|--------------------------------------------------------|-------------------------------------------------------------------------------------------------------------|
| URI Versioning                         | Simple. Client can cache resources easily              | Introduces a big footprint in the cod base as introducing breaking changes implies branching the entire API |
| Query Parameter Versioning             | Straight forward way to version a API                  | Most difficult to use for routing requests to the proper API version                                        |
| Header Versioning                      | It doesn't clutter the URI with versioning information | Requires additional custom headers                                                                          |
| Versioning through Content Negotiation |                                                        |                                                                                                             |

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




