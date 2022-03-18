package com.mongo.rs.core.testconfigs;

import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TransactionManagerTestConfig {

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
  //  @Bean
  //  ReactiveMongoTransactionManager transactionManager(ReactiveMongoDatabaseFactory factory) {
  //
  //    return new ReactiveMongoTransactionManager(factory);
  //  }
}