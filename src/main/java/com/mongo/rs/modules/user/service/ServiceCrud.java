package com.mongo.rs.modules.user.service;


import com.mongo.rs.modules.user.model.User;
import com.mongo.rs.modules.user.repo.ICrud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service("serviceCrud")
@RequiredArgsConstructor
public class ServiceCrud implements IServiceCrud {

  private final ICrud crud;

  /*╔══════════════════════════════╗
    ║   REACTIVE-MONGO-REPOSITORY  ║
    ╚══════════════════════════════╝*/
  @Override
  public Mono<User> save(User project) {

    return crud.save(project);
  }

  @Override
  public Flux<User> findAll() {

    return crud.findAll();
  }
}