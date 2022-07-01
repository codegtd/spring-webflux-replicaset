package com.mongo.rs.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;

@TestConfiguration
public class TestTransactionManagerConfig {

  /*╔════════════════════════════════════════════════╗
    ║           TRANSACTION-MANAGER-BEAN             ║
    ╠════════════════════════════════════════════════╣
    ║ THIS TRANSACTION-MANAGER-BEAN IS NECESSARY IN: ║
    ║ A) APP  -> @Configuration                      ║
    ║    - SRC/MAIN/JAVA/com/webflux/api/core/config ║
    ║                                                ║
    ║ B) TEST-APP -> @TestConfiguration              ║
    ║    - SRC/TEST/JAVA/com/webflux/api/core/config ║
    ╚════════════════════════════════════════════════╝*/
    @Bean
    ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {

      return new ReactiveMongoTransactionManager(factory);
    }
}