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
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

// ========================== PropertySource + ConfigurationProperties =============================
// Check - PropertySource: https://www.baeldung.com/configuration-properties-in-spring-boot
// Getter+Setter are CRUCIAL for PropertySource + ConfigurationProperties works properly
//@PropertySource(value = "classpath:application.yml", factory = YamlFileConverter.class)
//@ConfigurationProperties(prefix = "spring.data.mongodb")
//@Setter
//@Getter
// =================================================================================================
@Slf4j
//@Profile("rs1")
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user.repo"})
public class ConfigDb
//     extends AbstractReactiveMongoConfiguration
{
//  private String database;
//  private String host;
//  private String port;
//  private String username;
//  private String password;
//  private String authenticationDatabase;

  /*
   ╔════════════════════════════════════════════════╗
   ║           TRANSACTION-MANAGER-BEAN             ║
   ╠════════════════════════════════════════════════╣
   ║ THIS TRANSACTION-MANAGER-BEAN IS NECESSARY IN: ║
   ║ A) APP-CONTEXT  -> @Configuration              ║
   ║    - SRC/MAIN/JAVA/com/webflux/api/core/config ║
   ║                                                ║
   ║ B) TEST-CONTEXT -> @TestConfiguration          ║
   ║    - SRC/TEST/JAVA/com/webflux/api/core/config ║
   ╚════════════════════════════════════════════════╝
  */
  @Bean
  ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {

    return new ReactiveMongoTransactionManager(factory);
  }

  // 01) REACTIVE-MONGO-TEMPLATE-BEANS:
//  @Override
//  public MongoClient reactiveMongoClient() {
///*
//     ╔═══════════════════════════════╗
//     ║        SIMPLE-MONGO-DB        ║
//     ╚═══════════════════════════════╝
//*/
////    String connectionURI = "mongodb://" +
////         username + ":" + password + "@" +
////         host + ":" + port + "/" + database +
////         "?authSource=" + authenticationDatabase;
//
//        String connectionURI = "mongodb://"
//             + username + ":" + password +
//             "@" + host + ":" + port + "/" + database
//             + "?replicaSet=" + "mongo-rs"
//             + "&authSource=" + authenticationDatabase;
//
//    System.out.println("Connection --------------->  URI  ---------------> :" + connectionURI);
//
//    return MongoClients.create(connectionURI);
//  }


//  @Override
//  protected String getDatabaseName() {
//
//    return database;
//  }


//  @Bean
//  public ReactiveMongoTemplate reactiveMongoTemplate() {
//
//    return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
//  }



}