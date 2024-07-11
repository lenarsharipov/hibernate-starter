package com.lenarsharipov.dao;

import com.lenarsharipov.entity.User;
import jakarta.persistence.EntityManager;

public class UserRepository extends RepositoryBase<Long, User> {

    public UserRepository(EntityManager entityManager) {
        super(User.class, entityManager);
    }
}
