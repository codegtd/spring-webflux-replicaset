package com.mongo.rs.modules.user.service;

import com.mongo.rs.modules.user.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IServiceCrud {
  /*╔══════════════════════════════╗
      ║   REACTIVE-MONGO-REPOSITORY  ║
      ╚══════════════════════════════╝*/
  Mono<User> save(User project);


  Flux<User> findAll();
}