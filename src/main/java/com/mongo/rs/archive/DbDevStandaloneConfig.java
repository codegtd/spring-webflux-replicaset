package com.mongo.rs.archive;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

//@PropertySource(value = "classpath:application.yml", factory = YamlProcessor.class)
//@ConfigurationProperties(prefix = "spring.data.mongodb")
//@Setter
//@Getter
//@Profile("dev-std")
//@Slf4j
//@Configuration
//@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user"})
public class DbDevStandaloneConfig extends AbstractReactiveMongoConfiguration {
  private String host;
  private String port;
  private String authenticationDatabase;
  private String database;
  private String app_db_username;
  private String app_db_password;

  @Override
  public MongoClient reactiveMongoClient() {
    /*╔════════════════════════════════╗
      ║    STANDALONE-MONGO-DB  URL    ║
      ╠════════════════════════════════╩═══════════════════════════╗
      ║ mongodb://user:app_db_password@host:port/database?authSource=auth ║
      ╚════════════════════════════════════════════════════════════╝*/
    String appDbConnection =
         "mongodb://" +
              app_db_username + ":" + app_db_password +
              "@" + host + ":" + port +
              "/" + database +
              "?authSource=" + authenticationDatabase;


    System.out.println("Connection Standalone ---> " + appDbConnection);

    return MongoClients.create(appDbConnection);
  }


  @Override
  protected String getDatabaseName() {

    return database;
  }
}