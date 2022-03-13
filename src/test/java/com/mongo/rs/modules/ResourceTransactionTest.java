package com.mongo.rs.modules;

import com.github.javafaker.Faker;
import com.mongo.rs.core.annotations.ResourceTcContainerForTransactions;
import com.mongo.rs.core.testconfigs.TestCoreConfig;
import com.mongo.rs.core.utils.TestDbUtils;
import com.mongo.rs.modules.user.model.User;
import com.mongo.rs.modules.user.repo.TemplColections;
import com.mongo.rs.modules.user.service.IServiceCrud;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.mongo.rs.core.databuilders.UserBuilder.userNoID;
import static com.mongo.rs.core.databuilders.UserBuilder.userWithID;
import static com.mongo.rs.core.routes.Routes.TRANSACT_CLASSIC;
import static com.mongo.rs.core.routes.Routes.ROOT;
import static com.mongo.rs.core.utils.BlockhoundUtils.blockHoundTestCheck;
import static com.mongo.rs.core.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.mongo.rs.core.utils.RestAssureSpecs.responseSpecs;
import static com.mongo.rs.core.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpStatus.CREATED;


/*
  ╔══════════════════════════════════════════════════════════════════════╗
  ║                         SILAEV + TRANSACTIONS                        ║
  ╠══════════════════════════════════════════════════════════════════════╣
  ║ MongoDBContainer does replicaset init automatically:                 ║
  ║ a) add a static field with MongoDBContainer                          ║
  ║ b) run it in @BeforeAll and                                          ║
  ║ c) create a 'STATIC CLASS INITIALIZER' to set spring.data.mongodb.uri║
  ║ d) define @ContextConfiguration with 'static class Initializer'      ║
  ╚══════════════════════════════════════════════════════════════════════╝
*/
@Import({TestCoreConfig.class, TemplColections.class})
@Slf4j
@DisplayName("2 ResourceTransactTest")
@ResourceTcContainerForTransactions
public class ResourceTransactionTest {
  /*
╔════════════════════════════════════════════════════════════╗
║              TEST-TRANSACTIONS + TEST-CONTAINERS           ║
╠════════════════════════════════════════════════════════════╣
║ a) TRANSACTIONS IN MONGO-DB DEPENDS ON THE REPLICASET      ║
║    - MEANING: TRANSACTIONS ONLY WILL WORK WITH REPLICASET  ║
║                                                            ║
║ b) MongoDBContainer provides REPLICASET automatically      ║
║    - MEANING:                                              ║
║      B.1) TESTS MUST BE DONE WITH "MongoDBContainer"       ║
║      B.2) DO NOT USE TEST-CONTAINER-DOCKER-COMPOSE-MODULE  ║
╚════════════════════════════════════════════════════════════╝
*/
  // STATIC-@Container: one service for ALL tests -> SUPER FASTER
  // NON-STATIC-@Container: one service for EACH test

  final String enabledTest = "true";

  // MOCKED-SERVER: WEB-TEST-CLIENT(non-blocking client)'
  // SHOULD BE USED WITH 'TEST-CONTAINERS'
  // BECAUSE THERE IS NO 'REAL-SERVER' CREATED VIA DOCKER-COMPOSE
  @Autowired
  WebTestClient mockedWebClient;

  @Autowired
  TestDbUtils dbUtils;

  @Autowired
  IServiceCrud serviceCrud;



  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    globalBeforeAll();
    globalTestMessage(testInfo.getDisplayName(), "class-start");

    RestAssuredWebTestClient.reset();
    RestAssuredWebTestClient.requestSpecification =
         requestSpecsSetPath("http://localhost:8080" + ROOT);
    RestAssuredWebTestClient.responseSpecification = responseSpecs();
  }


  @AfterAll
  static void afterAll(TestInfo testInfo) {

    //    closeTcContainer();
    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(), "class-end");
  }


  @BeforeEach
  void beforeEach(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-start");

    User user1 = userNoID().create();

    User userWithId = userWithID().create();

    List<User> userList = asList(user1, userWithId);
    Flux<User> projectFlux = dbUtils.saveProjectList(userList);

    dbUtils.countAndExecuteFlux(projectFlux, 2);

  }


  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-end");

    dbUtils.cleanTestDb();
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("createUserTransaction")
  public void createUserTransaction() {

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);

    var newTaskName = Faker.instance()
                           .name()
                           .firstName();

    User user = userWithID().create();

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(user)
         .queryParam("taskNameInitial", newTaskName)

         .when()
         .post(TRANSACT_CLASSIC)

         .then()
         .log()
         .everything()

         .statusCode(CREATED.value())
         .body("id", equalTo(user.getId()))
         .body("name", equalTo(newTaskName))
         .body(matchesJsonSchemaInClasspath("contracts/save.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 3);
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("BHWorks")
  public void bHWorks() {

    blockHoundTestCheck();
  }

}