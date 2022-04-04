package com.mongo.rs.modules.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
@Document(collection = "user")
public class User implements Serializable {

  private static final long serialVersionUID = - 4708889289413262726L;

  @Id
  private String id;

//  @NotEmpty
  private String name;
}