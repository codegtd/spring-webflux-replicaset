package com.mongo.rs.core.config;


import com.mongo.rs.core.YamlFileConverter;
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
@Profile("std")
@Slf4j
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user.repo"})
public class DbConnectStandalone extends AbstractReactiveMongoConfiguration {
  private String host;
  private String port;
  private String authenticationDatabase;
  private String database;
  private String username;
  private String password;

  @Override
  public MongoClient reactiveMongoClient() {
    /*
         ╔═══════════════════════════════╗
         ║    STANDALONE-MONGO-DB URL    ║
         ╚═══════════════════════════════╝
    */
    //"mongodb://user:password@host:port/database?authSource=auth"
    String appPropertiesDbConnection = "mongodb://" +
         username + ":" + password + "@" +
         host + ":" + port + "/" + database +
         "?authSource=" + authenticationDatabase;


    System.out.println("Connection ---> appProperties ---> " + appPropertiesDbConnection);

    return MongoClients.create(appPropertiesDbConnection);
  }


  @Override
  protected String getDatabaseName() {

    return database;
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