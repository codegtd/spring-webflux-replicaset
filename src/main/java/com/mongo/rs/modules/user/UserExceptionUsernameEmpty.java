package com.mongo.rs.modules.user;

public class UserExceptionUsernameEmpty extends RuntimeException {

  public UserExceptionUsernameEmpty(final String message) {
    super(String.format("User[%s].notFound", message));
  }

}