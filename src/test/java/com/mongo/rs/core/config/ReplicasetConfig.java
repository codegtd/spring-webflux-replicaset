package com.mongo.rs.core.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import({
     DbUtilsConfig.class,
     TransactionManagerConfig.class
})
@TestConfiguration
public class ReplicasetConfig {

}