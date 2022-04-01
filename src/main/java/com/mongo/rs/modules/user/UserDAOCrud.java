package com.mongo.rs.modules.user;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository("repoCrud")
public interface UserDAOCrud extends ReactiveCrudRepository<User, String> {
}