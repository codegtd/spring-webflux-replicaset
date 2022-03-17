package com.mongo.rs.core.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
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
@Profile("rs3")
@Configuration
@EnableReactiveMongoRepositories(basePackages = {"com.mongo.rs.modules.user.repo"})
public class ConfigDb extends AbstractReactiveMongoConfiguration {

  @Value("${udemy.mongodb.replicaset.database}")
  private String db;

  @Value("${udemy.mongodb.replicaset.host}")
  private String host;

  @Value("${udemy.mongodb.replicaset.port}")
  private String port;

  @Value("${udemy.mongodb.replicaset.auth-db}")
  private String auth;

  @Value("${udemy.mongodb.replicaset.username}")
  private String username;

  @Value("${udemy.mongodb.replicaset.password}")
  private String password;

  @Value("${udemy.mongodb.replicaset.name}")
  private String rsname;

  @Value("${udemy.mongodb.replicaset.primary}")
  private String primary;

  @Override
  public MongoClient reactiveMongoClient() {

    final String connectionURI = "mongodb://"
         + username + ":" + password +
         "@" + primary + ":" + port + "/"
         + db
         + "?replicaSet=" + rsname
         + "&authSource=" + auth;

    System.out.println("Connection --------------->  URI ---------------> :" + connectionURI);

    return MongoClients.create(connectionURI);
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