package com.mongo.rs.modules.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service("serviceCrudImpl")
@RequiredArgsConstructor
public class UserServiceCrudImpl implements UserServiceCrud {

  private final UserDAOCrud userDAOCrud;

  /*╔══════════════════════════════╗
    ║   REACTIVE-MONGO-REPOSITORY  ║
    ╚══════════════════════════════╝*/
  @Override
  public Mono<User> save(User project) {

    return userDAOCrud.save(project);
  }

  @Override
  public Flux<User> findAll() {

    return userDAOCrud.findAll();
  }

  @Override
  public Flux<User> saveTransact(List<User> userList) {

    return userDAOCrud.saveAll(userList);
  }
}