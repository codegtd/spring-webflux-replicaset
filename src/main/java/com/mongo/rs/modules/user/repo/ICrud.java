package com.mongo.rs.modules.user.repo;

import com.mongo.rs.modules.user.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository("crud")
public interface ICrud extends ReactiveCrudRepository<User, String> {
}