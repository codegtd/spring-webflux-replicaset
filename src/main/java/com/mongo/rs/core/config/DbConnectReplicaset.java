package com.mongo.rs.core.config;


import com.mongo.rs.core.utils.YamlFileConverter;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
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
@Profile("rs-node3-gr")
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user.repo"})
public class DbConnectReplicaset extends AbstractReactiveMongoConfiguration {

  private String rootUri;
  private String db;
  private String rsName;
  private String authDb;
  private String username;
  private String password;

  @Override
  public MongoClient reactiveMongoClient() {
    /*
         ╔═══════════════════════════════╗
         ║    REPLICASET-MONGO-DB URL    ║
         ╚═══════════════════════════════╝
    */
    //"mongodb://mongo1:9042,mongo2:9142,mongo3:9242/api-db?replicaSet=docker-rs&authSource=admin"

    final String appPropertiesDbConnection =
         rootUri + "/" +
              db + "?" +
              "replicaSet=" + rsName +
              "&authSource=" + authDb;

    System.out.println("Connection ---> appProperties ---> " + appPropertiesDbConnection);

    return MongoClients.create(appPropertiesDbConnection);
  }


  @Override
  protected String getDatabaseName() {

    return db;
  }


  //  @Bean
  //  public ReactiveMongoTemplate reactiveMongoTemplate() {
  //    return new ReactiveMongoTemplate(reactiveMongoClient(),getDatabaseName());
  //  }
  //
  //
  @Bean
  ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {

    return new ReactiveMongoTransactionManager(factory);
  }
  //
  //
  //  @Bean
  //  public ReactiveGridFsTemplate reactiveGridFsTemplate() throws Exception {
  //    return new ReactiveGridFsTemplate(reactiveMongoDbFactory(),mongoConverter);
  //  }
}