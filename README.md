### Project Index

1. WebFlux:
    1. RestControllers


3. Application.Yml:
    1. Importation of properties:
        1. PropertySource
    2. Yml filesystem-Format
    3. Custom Logging.pattern.console


4. Application PROFILES:
    1. **Sufix:** Defining Application-sufix.Yml files
    2. **Annotations:** Selecting beans with specific Db properties
    3. **Groups:**
        1. Active group using _active-profile property_
        2. Grouping profiles [check link](https://www.baeldung.com/spring-profiles#4-profile-groups)


5. Docker:
    1. Compose
        1. _Specific file:_ **dev-compose.yml**
    2. Dockerfile
        1. _Specific file:_ **Dockerfile-dev**
    3. Batch Scripts:
        1. Parametric-scripts (env_variables)
            1. Parametric-scripts IDE execution
        2. Reusing bat-scripts:
            1. ex.: compose-up.bat using clean.bat


4. Testcontainers:
    * Containers
      * automatic replicaset allow test transactions
    * Compose


6. Architectural Strategy:
    * Screaming Architecture (core + modules)
    * CDC: Contract driven development
    * Testability:
        * TDD/CDC: Controllers

7. Spring Data  (findPostsByAuthor_Id)
    1. @Transactions
    3. Examples:
        1. [SpringaData Project](https://github.com/spring-projects/spring-data-examples)
        2. [MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb)
        3. [Reactive MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb/reactive)


8. Project Organization:
    1. Crud (ReactiveCrudRepository)


10. Exceptions:
    1. Exceptions must be in  Controller/Resource:
        1. Reason:
            1. Como stream pode ser manipulado por diferentes grupos de thread, caso um erro aconteça em uma thread que não é a que operou a controller,o ControllerAdvice não vai ser notificado "
            2. As stream can be handled by different thread groups, if an error happens on a thread other than the one that operated the controller, ControllerAdvice will not be notified "
        2. Source: medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de
           -sofrer-f92fb64517c3 // github.com/netshoes/blog-spring-reactive
    2. Custom
        1. Importation/validation of properties:
            1. @PropertySource
            2. @ConfigurationProperties:
                1. Automatic importation from PropertiesFile to Class-Instances-variables
                    1. Do not need "@Value" annotation


11. Tests - Junit 5:
    1. MultiThread/Parallel Test
        1. Aborted: Because server-costs in CI/CD
    2. RestAssured:
        1. RestAssuredWebTestClient:
            1. Reactive RestAssured
        2. JsonSchemaValidator
            1. Validate Responses
    3. Ordered tests (Junit 5.8.2)
    4. Suites
       1. source: https://howtodoinjava.com/junit5/junit5-test-suites-examples/
    5. Tags
    6. Get environment.getActiveProfiles() for detect ReplicasetProfile
    7. EnabledIfSystemProperty
       1. source: https://self-learning-java-tutorial.blogspot.com/2021/07/junit5-enabledifsystemproperty.html