package com.mongo.rs.core.utils;

import com.mongo.rs.modules.user.User;
import com.mongo.rs.modules.user.UserDAOCrud;
import com.mongo.rs.modules.user.UserDAOTempl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestDbUtils {

  @Autowired
  private UserDAOCrud userDaoCrud;

  @Autowired
  private UserDAOTempl userDaoTempl;


  public <E> void countAndExecuteFlux(Flux<E> flux, int totalElements) {

    StepVerifier
         .create(flux)
         .expectSubscription()
         .expectNextCount(totalElements)
         .verifyComplete();
  }

  public Flux<User> cleanDbAndSaveList(List<User> list) {

    return userDaoCrud.deleteAll()
                      .thenMany(Flux.fromIterable(list))
                      .flatMap(userDaoCrud::save)
                      .doOnNext(item -> userDaoCrud.findAll())
                      .doOnNext(item -> System.out.printf(
                            ">=> FindAll DB Elements >=>\n" +
                                 ">=> Saved 'User' in DB:\n" +
                                 "    |> ID: %s\n" +
                                 "    |> Name: %s\n\n"
                            ,
                            item.getId(),
                            item.getName()
                                                          ));
  }


  public <E> void checkFluxListElements(Flux<E> listFlux, List<E> listCompare) {

    StepVerifier.create(listFlux)
                .recordWith(ArrayList::new)
                .expectNextCount(listCompare.size())
                .thenConsumeWhile(listCompare::equals)
                .verifyComplete();
  }

  public void cleanTestDb() {

    StepVerifier
         .create(userDaoTempl.dropCollectionsTemplate())
         .expectSubscription()
         .verifyComplete();

    System.out.println(
         ">==================================================>\n" +
              ">===============> CLEAN-DB-TO-TEST >===============>\n" +
              ">==================================================>\n\n"
                      );
  }

}