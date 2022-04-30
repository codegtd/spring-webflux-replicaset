package com.mongo.rs.core.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;


//@Profile("dev-single-node-rs")
//@Profile("prod-three-nodes-rs")
@Profile("prod-three-nodes-rs-auth")
@Slf4j
@Configuration
public class DbTransactionManagerConfig {

  @Bean
  ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {

    return new ReactiveMongoTransactionManager(factory);
  }

}