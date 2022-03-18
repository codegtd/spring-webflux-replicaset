package com.mongo.rs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiDriver {

  //  static {
  //    BlockHound.install();
  //  }

  public static void main(String[] args) {

    SpringApplication.run(ApiDriver.class, args);
  }

}