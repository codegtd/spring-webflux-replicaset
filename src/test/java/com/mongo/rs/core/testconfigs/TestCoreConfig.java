package com.mongo.rs.core.testconfigs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@Import({
     TestDbUtilsConfig.class,
     TransactionManagerTestConfig.class
})
@TestConfiguration(value = "testCoreConfig")
public class TestCoreConfig {

}