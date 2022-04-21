### Project Index

1. WebFlux:
    1. RestControllers


2. Application.Yml:
    1. Importation of properties:
        1. [PropertySource](https://www.baeldung.com/configuration-properties-in-spring-boot)
    2. Yml filesystem-Format
    3. Custom Logging.pattern.console


3. Application PROFILES:
    1. **Sufix:** Defining Application-sufix.Yml files
    2. **Annotations:** Selecting beans with specific Db properties
    3. **Groups:**
        1. Active group using _active-profile property_
        2. Grouping profiles:
           1. Source:  [baeldung](https://www.baeldung.com/spring-profiles#4-profile-groups)


4. Docker:
    1. Compose
        1. _Specific file:_ **docker-compose.yml**
           1. Profiles:
              1. compose-dev-Replicaset: single-node
                 1. Source: [compose-replicaset-singlenode](https://stackoverflow.com/questions/60671005/docker-compose-for-mongodb-replicaset)
              2. compose-dev-Standalone: standalone-db
              3. compose-prod-replicaset: three nodes
        2. Environment:
           1. compose variables
           2. env_file (secrets)
              1. [Tutorial](https://www.youtube.com/watch?v=1je3VxDF67o)
        3. Secrets
           1. [secrets-with-docker-compose](https://www.rockyourcode.com/using-docker-secrets-with-docker-compose/)
           2. [secrets-during-development](https://blog.mikesir87.io/2017/05/using-docker-secrets-during-development/)
           3. [docker-secrets](https://docs.docker.com/engine/swarm/secrets/#use-secrets-in-compose)

    2. Dockerfile
        1. _Specific file:_ **Dockerfile**
    3. Batch Scripts:
        1. Parametric-scripts (env_variables)
            1. Parametric-scripts IDE execution
        2. Reusing bat-scripts:
            1. ex.: compose-up.bat using clean.bat


5. Testcontainers:
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


9. Exceptions:
    1. Exceptions must be in  Controller/Resource:
        1. Reason:
            1. Como stream pode ser manipulado por diferentes grupos de thread, caso um erro aconteça em uma thread que não é a que operou a controller,o ControllerAdvice não vai ser notificado "
            2. As stream can be handled by different thread groups, if an error happens on a thread other than the one that operated the controller, ControllerAdvice will not be notified "
        2. Sources:
           1.  [Medium](https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3)
           2. [Github](https://github.com/netshoes/blog-spring-reactive)
    2. Custom
        1. Importation/validation of properties:
            1. @PropertySource
            2. @ConfigurationProperties:
                1. Automatic importation from PropertiesFile to Class-Instances-variables
                    1. Do not need "@Value" annotation


10. Tests - Junit 5:
    1. MultiThread/Parallel Test
        1. Aborted: Because server-costs in CI/CD
    2. RestAssured:
        1. RestAssuredWebTestClient:
            1. Reactive RestAssured
        2. JsonSchemaValidator
            1. Validate Responses
    3. Ordered tests (Junit 5.8.2)
    4. Suites
       1. Source:  [junit5-test-suites-examples](https://howtodoinjava.com/junit5/junit5-test-suites-examples/)
    5. Tags
    6. System.setProperty:
       1. Get environment.getActiveProfiles() for detect ReplicasetProfile
    7. EnabledIfSystemProperty
       1. Source:  [junit5-enabledifsystempropert](https://self-learning-java-tutorial.blogspot.com/2021/07/junit5-enabledifsystemproperty.html)
    8. Spring Expression Language (SpEL) expressions:
       1. EnabledIf + SpEL
          1. Sources:
             1. [spring-5-enabledIf](https://www.baeldung.com/spring-5-enabledIf)
             2. [junit-5-conditional-test-execution](https://www.baeldung.com/junit-5-conditional-test-execution)