package com.mongo.rs.modules.user;


import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service("serviceCrud")
@RequiredArgsConstructor
public class ServiceCrudImpl implements ServiceCrud {

  private final RepoCrud crud;

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

  @Transactional
  @Override
  public Flux<User> saveTransact(List<User> userList) {

    return crud.saveAll(userList)
               .doOnNext(this::throwResponseStatusExceptionWhenEmptyName);
  }

  private void throwResponseStatusExceptionWhenEmptyName(User user) {

    if (StringUtil.isNullOrEmpty(user.getName())) {
      throw new ResponseStatusException(BAD_REQUEST, "Fail: Empty Name");
    }
  }
}