package com.mongo.rs.modules;

import com.mongo.rs.core.annotations.ResourceConfig;
import com.mongo.rs.core.testconfigs.TestCoreConfig;
import com.mongo.rs.core.testcontainer.container.TcContainerReplicasetTransaction;
import com.mongo.rs.core.utils.TestDbUtils;
import com.mongo.rs.modules.user.model.User;
import com.mongo.rs.modules.user.service.IServiceCrud;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.mongo.rs.core.databuilders.UserBuilder.userNoID;
import static com.mongo.rs.core.routes.Routes.*;
import static com.mongo.rs.core.testcontainer.container.TcContainerConfig.closeTcContainer;
import static com.mongo.rs.core.utils.BlockhoundUtils.blockHoundTestCheck;
import static com.mongo.rs.core.utils.BlockhoundUtils.blockhoundInstallWithAllAllowedCalls;
import static com.mongo.rs.core.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.mongo.rs.core.utils.RestAssureSpecs.responseSpecs;
import static com.mongo.rs.core.utils.TestUtils.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

/*
     ╔═══════════════════════════════╗
     ║ USING CONTAINERS IN THE TESTS ║
     ╠═══════════════════════════════╩════════════════╗
     ║ CONFLICT: TEST-CONTAINERS X DOCKER-CONTAINERS  ║
     ║           THEY DO NOT WORKS TOGETHER           ║
     ╠════════════════════════════════════════════════╩═════════╗
     ║A) TEST-CONTAINERS:                                       ║
     ║   A.1) STOP+CLEAN DOCKER-CONTAINERS  (DOCKER-BAT-SCRIPT) ║
     ║   A.2) SELECT THE TEST-PROFILE(testcontainer's)          ║
     ║   A.3) RUN THE TESTS                                     ║
     ║                                                          ║
     ║B) DOCKER-CONTAINERS:                                     ║
     ║   B.1) SELECT THE TEST-PROFILE(dockercontainer's)        ║
     ║   B.3) START DOCKER-CONTAINER (DOCKER-BAT-SCRIPT-PROFILE)║
     ║   B.4) RUN THE TESTS                                     ║
     ╚══════════════════════════════════════════════════════════╝
*/
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
@DisplayName("2 RS-Transaction-TcContainer")
@ResourceConfig
@ActiveProfiles("dockercontainer-standalone")
//@ActiveProfiles("testcontainer-container-rs-transact")
//@TcContainerReplicasetTransaction // TEST TRANSACTIONS
public class TransactionTests {
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

  @Autowired
  WebTestClient mockedWebClient;

  @Autowired
  TestDbUtils dbUtils;

  @Autowired
  IServiceCrud serviceCrud;

  User user1;
  private User userNoId;


  @BeforeAll
  static void beforeAll(TestInfo testInfo) {

    /*╔══════════════════════════╗
      ║        BLOCKHOUND        ║
      ╠══════════════════════════╣
      ║ Possible 'False-Positive ║
      ║    Out-date in GitHub    ║
      ╚══════════════════════════╝*/
    //    blockhoundInstallWithSpecificAllowedCalls();
//    blockhoundInstallWithAllAllowedCalls();



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
//    closeTcContainer();
  }


  @BeforeEach
  void beforeEach(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-start");

    user1 = userNoID().create();

    User user2 = userNoID().create();

    List<User> userList = asList(user1, user2);
    Flux<User> userFlux = dbUtils.saveItemsList(userList);

    dbUtils.countAndExecuteFlux(userFlux, 2);
  }


  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-end");
  }


  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("2 saveTransactionFail")
  public void saveTransactionFail() {

    userNoId = userNoID().create();
    User emptyUserTriggeringTransaction = userNoID().create();
    emptyUserTriggeringTransaction.setName("");
    List<User> userList = asList(userNoId, emptyUserTriggeringTransaction);

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
  @DisplayName("1 saveTransactionOK")
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
  @DisplayName("4 saveWithID")
  public void saveWithID() {

    User userIsolated = userNoID().create();

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(userIsolated)

         .when()
         .post(CRUD_SAVE)

         .then()
         .log()
         .everything()

         .statusCode(CREATED.value())
         .body("name", equalTo(userIsolated.getName()))
         .body(matchesJsonSchemaInClasspath("contracts/save.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 3);
  }

//  @Test
//  @EnabledIf(expression = enabledTest, loadContext = true)
//  @DisplayName("3 BHWorks")
//  public void bHWorks() {
//
//    blockHoundTestCheck();
//  }
}