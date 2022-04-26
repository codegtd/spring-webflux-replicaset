package archive;

import com.mongo.rs.core.annotations.ResourceConfig;
import com.mongo.rs.core.config.ReplicasetConfig;
import com.mongo.rs.core.utils.TestDbUtils;
import com.mongo.rs.modules.user.User;
import com.mongo.rs.modules.user.UserServiceCrud;
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
import static com.mongo.rs.core.testcontainer.container.TcContainerConfig.closeTcContainer;
import static com.mongo.rs.core.utils.RestAssureSpecs.requestSpecsSetPath;
import static com.mongo.rs.core.utils.RestAssureSpecs.responseSpecs;
import static com.mongo.rs.core.utils.TestUtils.*;
import static com.mongo.rs.modules.user.UserConfigRoutes.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

/*   ╔═══════════════════════════════╗
     ║ USING CONTAINERS IN THE TESTS ║
     ╠═══════════════════════════════╩════════════════╗
     ║ CONFLICT: TEST-CONTAINERS X DOCKER-CONTAINERS  ║
     ║           THEY DO NOT WORKS TOGETHER           ║
     ╠════════════════════════════════════════════════╩═════════╗
     ║A) TEST-CONTAINERS:                                       ║
     ║A.1) STOP+CLEAN DOCKER-CONTAINERS  (DOCKER-BAT-SCRIPT)    ║
     ║A.2) SELECT THE TEST-PROFILE FOR TESTS WITH TESTCONTAINERS║
     ║A.3) RUN THE TESTS                                        ║
     ║                                                          ║
     ║B) DOCKER-CONTAINERS (STANDALONE + REPLICASET):           ║
     ║B.1) SELECT THE TEST-PROFILE(dockercontainer's)           ║
     ║B.2) COMMENT @Container Instance variable                 ║
     ║B.3) START DOCKER-CONTAINER (DOCKER-BAT-SCRIPT-PROFILE)   ║
     ║B.4) RUN THE TESTS                                        ║
     ╚══════════════════════════════════════════════════════════╝*/
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
@Tags(value = {@Tag("standalone")})
@Import({ReplicasetConfig.class})
@DisplayName("3 Standalone Transactions")
@ResourceConfig
@ActiveProfiles("test-dev-std")
public class TransactionsStandaloneTest {
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

  final static String enabledTest = "true";

  @Autowired
  WebTestClient mockedWebClient;

  @Autowired
  TestDbUtils dbUtils;

  @Autowired
  UserServiceCrud serviceCrud;

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

    System.clearProperty("runTest");
    System.setProperty("runTest", enabledTest);

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
    closeTcContainer();
  }


  @BeforeEach
  void beforeEach(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-start");

    user1 = userNoID().create();

    User user2 = userNoID().create();

    List<User> userList = asList(user1, user2);
    Flux<User> userFlux = dbUtils.cleanDbAndSaveList(userList);

    dbUtils.countAndExecuteFlux(userFlux, 2);

  }


  @AfterEach
  void tearDown(TestInfo testInfo) {

    globalTestMessage(testInfo.getTestMethod()
                              .toString(), "method-end");
  }


  @Test
  @EnabledIf(expression =
       "#{" +
            "systemProperties[runTest] == 'true' && " +
            "environment.acceptsProfiles('test-dev-tc-rs','test-dev-rs')" +
            "}",
       loadContext = true)
  @Tag("replicaset-transaction")
  @DisplayName("2 saveRollback")
  public void saveRollback() {

    userNoId = userNoID().create();
    User lastUser = userNoID().create();
    lastUser.setName("");
    List<User> userList = asList(userNoId, lastUser);

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
  @EnabledIf(expression =
       "#{" +
            "systemProperties[runTest] == 'true' && " +
            "environment.acceptsProfiles('test-dev-tc-rs','test-dev-rs')" +
            "}",
       loadContext = true)
  @Tag("replicaset-transaction")
  @DisplayName("1 NoRollback")
  public void saveNoRollback() {

    userNoId = userNoID().create();
    User lastUser = userNoID().create();
    List<User> userList = asList(userNoId, lastUser);

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
              lastUser.getName()
                               ))
         .body(matchesJsonSchemaInClasspath("contracts/transaction.json"))
    ;

    dbUtils.countAndExecuteFlux(serviceCrud.findAll(), 4);
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @Tags(value = {
       @Tag("replicaset-transaction"),
       @Tag("standalone")})
  @DisplayName("3 saveWithID")
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

}