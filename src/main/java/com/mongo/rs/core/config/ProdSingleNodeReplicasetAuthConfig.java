package com.mongo.rs.core.config;


import com.mongo.rs.core.YamlProcessor;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.nio.file.Files;
import java.nio.file.Paths;

@PropertySource(value = "classpath:application.yml", factory = YamlProcessor.class)
@ConfigurationProperties(prefix = "db.mongodb.replicaset")
@Setter
@Getter
@Profile("prod-single-node-rs-auth")
@Import({DbTransactionManagerConfig.class})
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user"})
public class ProdSingleNodeReplicasetAuthConfig extends AbstractReactiveMongoConfiguration {

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
      ╠══════════════════════════════════════════════════════════════════════╣
      ║ mongodb://username:password                                          ║
      ║           @mongo1:9042,mongo2:9142,mongo3:9242/api-db                ║
      ║           ?replicaSet=docker-rs&authSource=root_admin_db.txt                     ║
      ╚══════════════════════════════════════════════════════════════════════╝*/
    final String connection =
         "mongodb://" +
              //              username + ":" + password +
              username + ":" + getPasswordFromDockerSecretFolder(password) +
              // ROOTURI: replicasetPrimary + ":" + replicasetPort
              "@" + rootUri +
              // DATABASE: OMMITT/SUPRESS database when it should be created late/after
              "/" + database +
              "?replicaSet=" + replicasetName +
              "&authSource=" + authDb;


    /*╔══════════════════════════════════════════════════════════════════════╗
      ║      REPLICASET-3-NODES-MONGO-DB PRODUCTION URL (UDEMY COURSE)       ║
      ╚══════════════════════════════════════════════════════════════════════╝*/
    // "mongodb://" +
    //      replicasetUsername + ":" + replicasetPassword +
    //      "@" + replicasetPrimary + ":" + replicasetPort +
    //      "/" + database +
    //      "?replicaSet=" + replicasetName +
    //      "&authSource=" + replicasetAuthenticationDb

    System.out.println(
         "DevThreeNodesReplicasetAuth ---> " + connection);

    return MongoClients.create(connection);
  }


  @Override
  protected String getDatabaseName() {

    return database;
  }

  @SneakyThrows
  private String getPasswordFromDockerSecretFolder(String secretName) {
    // 1) Creates secret-path-folder
    String secretPath = "/run/secrets/" + secretName;
    String ret = "";

//    System.out.println(secretPath.concat(" +++secretPath1"));

    // 2) if a secret is present, inject as a 'secret' (readAllBytes from secretPath)
    if (Files.exists(Paths.get(secretPath))) {
//      System.out.println(Files.exists(Paths.get(secretPath)) + " +++secretPath2");

      final byte[] readFile = Files.readAllBytes(Paths.get(secretPath));

      ret = new StringBuilder(
           new String(
                readFile))
           .toString();
    }

//    System.out.println(Files.exists(Paths.get(secretPath)) + " +++ret");
    return ret;
  }
}