package com.mongo.rs.archive;


import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

//@PropertySource(value = "classpath:application.yml", factory = YamlProcessor.class)
//@ConfigurationProperties(prefix = "db.mongodb.replicaset")
//@Setter
//@Getter
//@Profile("rs-three-nodes")
//@Import({TransactionManagerConfig.class})
//@Slf4j
//@Configuration
//@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user"})
public class ReplicasetThreeNodesConfig extends AbstractReactiveMongoConfiguration {

  private String rootUri;
  private String database;
  private String replicasetName;
  private String authenticationDatabase;
  private String username;
  private String password;

  @Autowired
  private Environment environment;

  @Override
  public MongoClient reactiveMongoClient() {
    /*╔═════════════════════════════════════════════════════════════════╗
      ║ REPLICASET-3-NODES-MONGO-DB PRODUCTION URL (NO USER + PASSWORD) ║
      ╠═════════════════════════════════════════════════════════════════╣
      ║ mongodb://mongo1:9042,mongo2:9142,mongo3:9242/api-db            ║
      ║           ?replicaSet=docker-rs&authSource=app_db_name          ║
      ╚═════════════════════════════════════════════════════════════════╝*/


    final String
         connection =
         rootUri +
              "/" + database +
              "?replicaSet=" + replicasetName +
              "&authSource=" + authenticationDatabase;

    System.out.println("Profile: "+environment.getActiveProfiles()[0]+" | Uri: " + connection);

    return MongoClients.create(connection);
  }


  @Override
  protected String getDatabaseName() {

    return database;
  }
}