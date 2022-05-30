## Spring-WebFlux-Replicaset

### Table of Contents
* [WebFlux](#webflux)
* [Application.Yml](#application_yml)
* [Application Profiles](#application-profiles)
* [Docker](#docker)
* [Secrets](#docker-secrets)
* [Mongo-Replicaset](#docker-mongo-replicaset)
* [Testcontainers](#testcontainers)
* [Architectural Strategy](#architectural-strategy)
* [Spring Data](#spring-data-findPostsByAuthor_Id)
* [Project Organization](#project-organization)
* [Exceptions](#exceptions)
* [Tests Junit 5](#tests-junit-5)
* [GitGuardian pre commit-githook](#gitguardian-with-git)

### WebFlux
1. RestControllers

### Application_yml
1. Importation of properties:
    1. [PropertySource](https://www.baeldung.com/configuration-properties-in-spring-boot)
2. Yml filesystem-Format
3. Custom Logging.pattern.console

### Application PROFILES
1. **Sufix:** Defining Application-sufix.Yml files
2. **Annotations:** Selecting beans with specific Db properties
3. **Groups:**
    1. Active group using _active-profile property_
    2. Grouping profiles:
       1. [baeldung](https://www.baeldung.com/spring-profiles#4-profile-groups)

### Docker
1. Compose
    1. _Specific file:_ **docker-compose.yml**
       1. Profiles:
          1. compose-dev-Replicaset: single-node
             1. [compose-replicaset-singlenode](https://stackoverflow.com/questions/60671005/docker-compose-for-mongodb-replicaset)
          2. compose-dev-Standalone: standalone-db
          3. compose-prod-replicaset: three nodes
    2. Environment:
       1. compose variables
       2. Modular env_files
          1. [Tutorial](https://www.youtube.com/watch?v=1je3VxDF67o)
    3. Running SH-Scripts
2. Dockerfile
    1. _Specific file:_ **Dockerfile**
3. Batch Scripts:
    1. Parametric-scripts (env_variables)
        1. Parametric-scripts IDE execution
    2. Reusing bat-scripts:
        1. ex.: compose-up.bat using clean.bat
4. SH Scripts:
    1. Running
    2. Environment variables
        1. [Loading](https://zwbetz.com/set-environment-variables-in-your-bash-shell-from-a-env-file/)
        2. [Delete](https://www.baeldung.com/linux/delete-shell-env-variable)
5. Docker images safety
   1. Aspects:
      1. Delete the \run\secrets\<all-secrets> does not solve the problem because the historial layers in the docker 
         can contains some credentials
   2. Source:
      1. [Finding leaked credentials in Docker images](https://www.youtube.com/watch?v=SOd_XMIGRqo&t=435s)

### Docker-Secrets
1. Idea:
   1. Docker secrets needs to be supported by the image that will be use this it. Some images have it, such as:
      1. MySQL offical docker-image
      2. MongoDb official docker-Image
   2. The webapp should support it as well, using the library:
      1. [Spring Boot Docker Secret Starter](https://github.com/rozidan/docker-secret-spring-boot-starter#spring-boot-docker-secret-starter)
2. How secrets will work?
   1. Problem to solve:
      1. When env_variables read the 'sensitive data'(such as Password + login + tokens, etc..) externally (env.file)
         1. It is mandatory, exclude this env-file from VCS (git.ignore)
         2. The content (ex. password) will be visible in containers because env_variables (docker inspect 
            container) allow that;
   2. How to solve it?: Docker-Secrets:
      1. It will hide this sensitive content, make this content hidden in docker-inspect;
      2. It will substitute the env_vars content for the "path of the secrets", not the content of it, HOWEVER, using it.
         1. The env-var in compose must have the suffix _FILE
3. Secrets Storage 
   1. Default-Storage Folder:
      1. '/run/secrets', of course, inside the service/worker is using them
   2. Custom Target Folder:
      1. xxxx
   3. Each SECRET own its file, in the Storage-Folder
   4. Deletion/removing forbidden:
      1. [" After you create a secret,you cannot remove a secret that a service is using. However, 
         you can grant or revoke a running service's access to secrets using docker service update ."](https://docs.docker.com/engine/swarm/secrets/#advanced-example-use-secrets-with-a-wordpress-service)
4. Sources:
   1. [secrets-with-docker-compose](https://www.rockyourcode.com/using-docker-secrets-with-docker-compose/)
   2. [secrets-during-development](https://blog.mikesir87.io/2017/05/using-docker-secrets-during-development/)
   3. [docker-secrets](https://docs.docker.com/engine/swarm/secrets/#use-secrets-in-compose)
   4. [earthly.dev](https://earthly.dev/blog/docker-secrets/)
   5. [secured-mongodb-container](https://medium.com/@leonfeng/set-up-a-secured-mongodb-container-e895807054bd)
   6. [Docker Secret in Microservice](https://blogmilind.wordpress.com/2018/03/14/docker-secret-in-microservice/)

### Docker-Mongo-Replicaset
1. Singlenode
   1. Idea:
      1. Three nodes is the ideal, either for redundance, or transactions;
         1. However
            1. It will increasy A LOT the HOST-SERVER
         2. Singlenode IS CHEAPER and allow transactions, as well:
           * It is cheaper because requires only ONE VM in the cloud
               + IT CAN _**DECREASE**_ THE COST "CONSIDERABLY"
   3. Types:
      1. NoAuthentication
      2. Authenticated
         1. dynamic mongodb-keyfile (generate as a service in compose) 
         2. [3Nodes - Base for single node authentication](https://www.youtube.com/watch?v=-XzMfd4XQak)
            1. [GitHub](https://github.com/willitscale/learning-docker)
2. Three nodes:
   1. **NOTE**: 
      1. This replica set is for *Local Development* purposes ONLY. 
         1. Run multiple nodes within a single machine is an anti-pattern, and MUST BE AVOIDED in Production.
      2. Multiple nodes requires:
         1. Multiple Vm's in the cloud
            1. IT CAN _**INCREASE**_ THE COST "CONSIDERABLY"
   2. Types:
      1. NoAuthentication
      2. Three nodes - Authenticated:
         1. [ProfileProduction](https://sntnupl.com/mongodb-replicaset-for-development-using-docker)
         2. [yowko](https://github.com/yowko/docker-compose-mongodb-replica-set-with-auth/blob/master/docker-compose.yaml)
         3. [prashix](https://prashix.medium.com/setup-mongodb-replicaset-with-authentication-enabled-using-docker-compose-5edd2ad46a90)
         4. [keyfile](https://www.educba.com/mongodb-keyfile/)
         5. [mongo-authentication](https://mkyong.com/mongodb/mongodb-authentication-example/)
         6. [MongoCli](https://www.mongodb.com/docs/manual/reference/configuration-file-settings-command-line
            -options-mapping/#std-label-conf-file-command-line-mapping/)
3. StandAlone - ProfileDevelopment
   1. Only archive because:
      1. it does not allow transactions, therefore:
         1. it does not run queues(CREATE+DELETE+UPDATE)

### Testcontainers
* Containers
  * Automatic replicaset allow test transactions
* Compose
  * MongoDb StandAlone

### Architectural Strategy
* Screaming Architecture (core + modules)
* CDC: Contract driven development
* Testability:
    * TDD with CDC: 
      * Resources/Controllers

### Spring Data findPostsByAuthor_Id
1. @Transactions
3. Examples:
    1. [SpringaData Project](https://github.com/spring-projects/spring-data-examples)
    2. [MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb)
    3. [Reactive MongoDB](https://github.com/spring-projects/spring-data-examples/tree/main/mongodb/reactive)

### Project Organization
1. Crud (ReactiveCrudRepository)

### Exceptions
1. Exceptions must be in  Controller/Resource:
    1. Reason:
        1. Como stream pode ser manipulado por diferentes grupos de thread, caso um erro aconteça em uma thread que não é a que operou a controller,o ControllerAdvice não vai ser notificado "
    2. As stream can be handled by different thread groups, if an error happens on a thread other than the one that operated the controller, ControllerAdvice will not be notified "
2. Articles:
   1.  [Medium](https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3)
   2. [Github](https://github.com/netshoes/blog-spring-reactive)
3. Custom
    1. Importation/validation of properties:
        1. @PropertySource
        2. @ConfigurationProperties:
            1. Automatic importation from PropertiesFile to Class-Instances-variables
                1. Do not need "@Value" annotation

### Tests Junit 5
1. MultiThread/Parallel Test
    1. Aborted: Because server-costs in CI/CD
2. RestAssured:
    1. RestAssuredWebTestClient:
        1. Reactive RestAssured
    2. JsonSchemaValidator - CDD Contracts Driven Development
        1. Validate Responses
3. Ordered tests (Junit 5.8.2)
4. Suites
   1. [junit5-test-suites-examples](https://howtodoinjava.com/junit5/junit5-test-suites-examples/)
5. Tags
6. System.setProperty:
   1. Get environment.getActiveProfiles() for detect ReplicasetProfile
7. EnabledIfSystemProperty
   1. [junit5-enabledifsystempropert](https://self-learning-java-tutorial.blogspot.com/2021/07/junit5-enabledifsystemproperty.html)
8. Spring Expression Language (SpEL) expressions:
   1. EnabledIf + SpEL
      2. [spring-5-enabledIf](https://www.baeldung.com/spring-5-enabledIf)
      3. [junit-5-conditional-test-execution](https://www.baeldung.com/junit-5-conditional-test-execution)


9. ### GitGuardian with Git
   1. Idea:
      1. Concept:
         1. GitGuardian shield (ggshield) is a CLI application that runs in your local environment or in a CI.
         2. The purpouse is detect more than 300 types of secrets, as well as other potential security vulnerabilities or policy breaks.
      2. In a nutshell:
         1. Scan files searching SECRETS **_"BEFORE"_** commit/push 
            1. Avoiding to send SECRETS to web (ex.: docker-hub, github, etc...)
   2. Scan-time - the scan can be done:
      1. Pre-commit: prevent send the secrets in GitHistory 
      2. Pre-push: needs to clena githistory (not recommended)
      3. [Pre-commit vs Pre-push](https://youtu.be/uc70CE1MXvM)
      4. docker images using GGShield-CLI
   3. GitGuardian - API-KEY:
      1. export:
         1. use export to send it as env-var in terminal-session  
      2. env-var-file:
         1. create env-var-file inside the repository
            1. ADD IT IN GIT-IGNORE!!
   4. PIP:
      1. Installation:
         1. [Install phyton](https://www.python.org/downloads/)
         2. python.exe -m pip install --upgrade pip
      2. PIP is used to install:
         1. pre-commit: pip install pre-commit
         2. ggshield: pip install ggshield
   5. Enabling GGshield in the Git-Local-Repository 
      1. [Tutorial](https://youtu.be/ySTG2NODQCg)
         
   6. Source:
      1. [Detect secrets with a pre-commit-githook {export}](https://youtu.be/8bDKn3y7Br4)
      2. [Detect secrets with a pre-push-githook {env-var-file}](https://youtu.be/uc70CE1MXvM)