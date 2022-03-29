package com.mongo.rs.core.databuilders;

import com.github.javafaker.Faker;
import com.mongo.rs.modules.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Locale;

@Builder
@Getter
public class UserBuilder {

  private static final Faker faker = new Faker(new Locale("en-CA.yml"));
  private final User user;


  public static String createFakeUniqueRandomId() {

    return faker.regexify("PP[a-z0-9]{24}");
  }


  public static UserBuilder userNoID() {

    User user = new User();

    user.setName(faker.name()
                      .fullName());

    return UserBuilder.builder()
                      .user(user)
                      .build();
  }

  public static UserBuilder userWithID() {

    User proj = new User();

    proj.setId(createFakeUniqueRandomId());
    proj.setName(faker.name()
                      .fullName());

    return UserBuilder.builder()
                      .user(proj)
                      .build();
  }


  public User create() {

    return this.user;
  }
}