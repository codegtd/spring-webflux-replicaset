package com.mongo.rs.core.utils;

import com.mongo.rs.modules.user.model.User;
import com.mongo.rs.modules.user.repo.ICrud;
import com.mongo.rs.modules.user.repo.TemplColections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class TestDbUtils {

  @Autowired
  ICrud userCrudRepo;

  @Autowired
  TemplColections repoColections;


  public <E> void countAndExecuteFlux(Flux<E> flux, int totalElements) {

    StepVerifier
         .create(flux)
         .expectSubscription()
         .expectNextCount(totalElements)
         .verifyComplete();
  }

  public Flux<User> saveProjectList(List<User> projectList) {

    return userCrudRepo.deleteAll()
                       .thenMany(Flux.fromIterable(projectList))
                       .flatMap(userCrudRepo::save)
                       .doOnNext(item -> userCrudRepo.findAll())
                       .doOnNext(item -> System.out.printf(
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
         .create(repoColections.dropCollectionsTemplate())
         .expectSubscription()
         .verifyComplete();

    System.out.println(
         ">==================================================>\n" +
              ">===============> CLEAN-DB-TO-TEST >===============>\n" +
              ">==================================================>\n\n"
                      );
  }

}