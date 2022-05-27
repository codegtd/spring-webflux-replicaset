package com.mongo.rs.modules.user;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.mongo.rs.modules.user.UserRoutes.*;
import static io.netty.util.internal.StringUtil.isNullOrEmpty;
import static org.springframework.http.HttpStatus.*;

// ==> EXCEPTIONS IN CONTROLLER:
// *** REASON: IN WEBFLUX, EXCEPTIONS MUST BE IN CONTROLLER - WHY?
//     - "Como stream pode ser manipulado por diferentes grupos de thread,
//     - caso um erro aconteça em uma thread que não é a que operou a controller,
//     - o ControllerAdvice não vai ser notificado "
//     - https://medium.com/nstech/programa%C3%A7%C3%A3o-reativa-com-spring-boot-webflux-e-mongodb-chega-de-sofrer-f92fb64517c3
//     - https://github.com/netshoes/blog-spring-reactive/blob/master/src/main/java/com/netshoes/products/gateways/http/ProductController.java
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
public class UserResourceCrud {

  private final UserServiceCrud serviceCrudImpl;

  @PostMapping(CRUD_SAVE)
  @ResponseStatus(CREATED)
  public Mono<User> save(@RequestBody User user) {

    return
         serviceCrudImpl
              .save(user)
              .doOnNext(this::throwSimpleExceptionWhenEmptyName)
         ;
  }

  @GetMapping(CRUD_FINDALL)
  @ResponseStatus(OK)
  public Flux<User> findAll() {

    return serviceCrudImpl.findAll();
  }

  @Transactional
  @PostMapping(CRUD_SAVE_TRANSACT)
  @ResponseStatus(CREATED)
  public Flux<User> saveTransac(@RequestBody List<User> userList) {

    return
         serviceCrudImpl
              .saveTransact(userList)
              .doOnNext(this::throwSimpleExceptionWhenEmptyName)
         ;
  }

  private void throwSimpleExceptionWhenEmptyName(User user) {

    if (isNullOrEmpty(user.getName())) {
      throw new ResponseStatusException(BAD_REQUEST, "Fail: Empty Name");
    }
  }

}