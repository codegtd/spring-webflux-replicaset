package com.mongo.rs.modules.user;


import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface UserServiceCrud {
  /*╔══════════════════════════════╗
    ║   REACTIVE-MONGO-REPOSITORY  ║
    ╚══════════════════════════════╝*/
  Mono<User> save(User project);


  Flux<User> findAll();


  Flux<User> saveTransact(List<User> userList);
}