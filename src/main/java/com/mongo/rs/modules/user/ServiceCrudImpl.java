package com.mongo.rs.modules.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static io.netty.util.internal.StringUtil.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service("serviceCrudImpl")
@RequiredArgsConstructor
public class ServiceCrudImpl implements ServiceCrud {

  private final RepoCrud repoCrud;

  /*╔══════════════════════════════╗
    ║   REACTIVE-MONGO-REPOSITORY  ║
    ╚══════════════════════════════╝*/
  @Override
  public Mono<User> save(User project) {

    return repoCrud.save(project);
  }

  @Override
  public Flux<User> findAll() {

    return repoCrud.findAll();
  }

  @Transactional
  @Override
  public Flux<User> saveTransact(List<User> userList) {

    return repoCrud.saveAll(userList)
                   //               .doOnNext(this::throwResponseStatusExceptionWhenEmptyName)
                   .doOnNext(item -> {
                 throwResponseStatusExceptionWhenEmptyName(item);
               })
         ;
  }

  private void throwResponseStatusExceptionWhenEmptyName(User user) {

    if (isNullOrEmpty(user.getName())) {
      throw new ResponseStatusException(BAD_REQUEST, "Fail: Empty Name");
    }
  }
}