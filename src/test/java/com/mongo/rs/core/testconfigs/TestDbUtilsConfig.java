package com.mongo.rs.core.testconfigs;

import com.mongo.rs.core.utils.TestDbUtils;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestDbUtilsConfig {

  @Bean
  public TestDbUtils testDbUtils() {

    return new TestDbUtils();
  }

}