package com.mongo.rs.modules;

import com.mongo.rs.core.annotations.ResourceTestcontainerContainerForTransactions;
import com.mongo.rs.core.testconfigs.TestCoreConfig;
import com.mongo.rs.core.utils.TestDbUtils;
import com.mongo.rs.modules.user.model.User;
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
import static com.mongo.rs.core.routes.Routes.*;
import static com.mongo.rs.core.utils.BlockhoundUtils.blockHoundTestCheck;
import static com.mongo.rs.core.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.mongo.rs.core.utils.RestAssureSpecs.responseSpecs;
import static com.mongo.rs.core.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.*;


/*
  ╔══════════════════════════════════════════════════════════════════════╗
  ║    SILAEV + TRANSACTIONS + REPLICASET + TEST-CONTAINER-CONTAINER     ║
  ╠══════════════════════════════════════════════════════════════════════╣
  ║ MongoDBContainer does REPLICASET init automatically:                 ║
  ║  a) Static-Extension-Annotation Class starts automatically           ║
  ║  b) the MongoDBContainer this TcContainer has a Replicaset           ║
  ║  c) This Class gets the URI from this MongoDBContainer               ║
  ║  d) and setting this URI in 'Properties of the Test'                 ║
  ╚══════════════════════════════════════════════════════════════════════╝
*/
@Import({TestCoreConfig.class})
@Slf4j
@DisplayName("2 ResourceReplicaSetTest")
@ResourceTestcontainerContainerForTransactions
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

  private User user1;
  private User user2;
  private User userNoId;


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

    globalAfterAll();
    globalTestMessage(testInfo.getDisplayName(), "class-end");
  }


  @BeforeEach
  void beforeEach(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-start");

    user1 = userNoID().create();

    user2 = userNoID().create();

    List<User> userList = asList(user1, user2);
    Flux<User> userFlux = dbUtils.saveProjectList(userList);

    dbUtils.countAndExecuteFlux(userFlux, 2);
  }


  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-end");
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("2.0 saveTransactionFail")
  public void saveTransactionFail() {

    userNoId = userNoID().create();
    User extraUser = userNoID().create();
    extraUser.setName("");
    List<User> userList = asList(userNoId, extraUser);

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(userList)

         .when()
         .post(CRUD_SAVE_TRANSACT)

         .then()
         .log()
         .everything()

         .statusCode(BAD_REQUEST.value())

         .body(matchesJsonSchemaInClasspath("contracts/exception.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 2);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("1.0 saveTransactionOK")
  public void saveTransactionOK() {

    userNoId = userNoID().create();
    User extraUser = userNoID().create();
    List<User> userList = asList(userNoId, extraUser);

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(userList)

         .when()
         .post(CRUD_SAVE_TRANSACT)

         .then()
         .log()
         .everything()

         .statusCode(CREATED.value())
         .body("size()", is(2))
         .body("$", hasSize(2))
         .body("name", hasItems(
              userNoId.getName(),
              extraUser.getName()
                               ))
         .body(matchesJsonSchemaInClasspath("contracts/transaction.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 4);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("3.0 BHWorks")
  public void bHWorks() {

    blockHoundTestCheck();
  }

}