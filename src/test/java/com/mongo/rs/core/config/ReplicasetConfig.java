package com.mongo.rs.core.config;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import({
     DbUtilsConfig.class,
     TransactionManagerConfig.class
})
@TestConfiguration
public class ReplicasetConfig {

}