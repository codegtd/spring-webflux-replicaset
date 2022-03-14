package com.mongo.rs.modules;

import com.mongo.rs.core.annotations.ResourceTestcontainerCompose;
import com.mongo.rs.core.testconfigs.TestDbUtilsConfig;
import com.mongo.rs.core.testcontainer.compose.TcComposeConfig;
import com.mongo.rs.core.utils.TestDbUtils;
import com.mongo.rs.modules.user.model.User;
import com.mongo.rs.modules.user.service.IServiceCrud;
import io.restassured.module.webtestclient.RestAssuredWebTestClient;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.EnabledIf;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
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
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Import({TestDbUtilsConfig.class})
@DisplayName("1 ResourceCrudTest")
@ResourceTestcontainerCompose
public class ResourceCrudTest {

  // STATIC-@Container: one service for ALL tests -> SUPER FASTER
  // NON-STATIC-@Container: one service for EACH test
//  @Container
//  private static final DockerComposeContainer<?> compose = new TcComposeConfig().getContainer();

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

    userNoId = userNoID().create();

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
  @DisplayName("3 saveWithID")
  public void saveWithID() {

    RestAssuredWebTestClient
         .given()
         .webTestClient(mockedWebClient)

         .body(user1)

         .when()
         .post(CRUD_SAVE)

         .then()
         .log()
         .everything()

         .statusCode(CREATED.value())
         .body("id", equalTo(user1.getId()))
         .body("name", equalTo(user1.getName()))
         .body(matchesJsonSchemaInClasspath("contracts/save.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("2 FindAll")
  public void FindAll() {

    dbUtils.checkFluxListElements(
         serviceCrud.findAll()
                    .flatMap(Flux::just),
         asList(user1, user2)
                                 );

    RestAssuredWebTestClient

         .given()
         .webTestClient(mockedWebClient)

         .when()
         .get(CRUD_FINDALL)

         .then()
         .log()
         .everything()

         .statusCode(OK.value())
         .body("size()", is(2))
         .body("$", hasSize(2))
         .body("name", hasItems(user1.getName(), user2.getName()))
         .body(matchesJsonSchemaInClasspath("contracts/findall.json"))
    ;
  }

  @Test
  @EnabledIf(expression = enabledTest, loadContext = true)
  @DisplayName("1 BHWorks")
  public void bHWorks() {

    blockHoundTestCheck();
  }
}