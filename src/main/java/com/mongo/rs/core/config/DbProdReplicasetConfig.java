package com.mongo.rs.core.config;


import com.mongo.rs.core.YamlFileConverter;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

// ========================== PropertySource + ConfigurationProperties =============================
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Getter+Setter are CRUCIAL for PropertySource + ConfigurationProperties works properly
@PropertySource(value = "classpath:application.yml", factory = YamlFileConverter.class)
@ConfigurationProperties(prefix = "db.mongodb.replicaset")
@Setter
@Getter
// =================================================================================================

@Profile("prod-rs")
@Import({DbTransactionManagerConfig.class})
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user"})
public class DbProdReplicasetConfig extends AbstractReactiveMongoConfiguration {

  private String rootUri;
  private String database;
  private String replicasetName;
  private String authDb;
  private String username;
  private String password;

  @Override
  public MongoClient reactiveMongoClient() {
    /*╔══════════════════════════════════════════════════════════════════════╗
      ║ REPLICASET-3-NODES-MONGO-DB PRODUCTION URL (FULL:NO USER + PASSWORD) ║
      ╠══════════════════════════════════════════════════════════════════════╩╗
      ║ mongodb://username:password                                           ║
      ║           @mongo1:9042,mongo2:9142,mongo3:9242/api-db                 ║
      ║           ?replicaSet=docker-rs&authSource=admin                      ║
      ╚═══════════════════════════════════════════════════════════════════════╝*/
    final String appDbConnectionFull =
         "mongodb://" +
              username + ":" + password +
              "@" + rootUri + // replicasetPrimary + ":" + replicasetPort
              "/" + database +
              "?replicaSet=" + replicasetName +
              "&authSource=" + authDb;
    System.out.println("Connection Replicaset Full ---> " + appDbConnectionFull);

    /*╔═════════════════════════════════════════════════════════════════╗
      ║ REPLICASET-3-NODES-MONGO-DB PRODUCTION URL (UDEMY COURSE)       ║
      ╠═════════════════════════════════════════════════════════════════╩═╗
      ║ mongodb://mongo1:9042,mongo2:9142,mongo3:9242/api-db              ║
      ║           ?replicaSet=docker-rs&authSource=admin                  ║
      ╚═══════════════════════════════════════════════════════════════════╝*/
    // "mongodb://" +
    //      replicasetUsername + ":" + replicasetPassword +
    //      "@" + replicasetPrimary + ":" + replicasetPort +
    //      "/" + database +
    //      "?replicaSet=" + replicasetName +
    //      "&authSource=" + replicasetAuthenticationDb

    /*╔═════════════════════════════════════════════════════════════════╗
      ║ REPLICASET-3-NODES-MONGO-DB PRODUCTION URL (NO USER + PASSWORD) ║
      ╠═════════════════════════════════════════════════════════════════╩═╗
      ║ mongodb://mongo1:9042,mongo2:9142,mongo3:9242/api-db              ║
      ║           ?replicaSet=docker-rs&authSource=admin                  ║
      ╚═══════════════════════════════════════════════════════════════════╝*/
    //        final String appDbConnectionSimple =
    //             rootUri +
    //                  "/" + db +
    //                  "?replicaSet=" + rsName +
    //                  "&authSource=" + authDb;
    //    System.out.println("Connection Replicaset Simple ---> " + appDbConnectionSimple);

    return MongoClients.create(appDbConnectionFull);
  }


  @Override
  protected String getDatabaseName() {

    return database;
  }
}