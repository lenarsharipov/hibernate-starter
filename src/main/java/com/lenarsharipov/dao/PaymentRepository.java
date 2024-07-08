package com.lenarsharipov.dao;

import com.lenarsharipov.entity.Payment;
import jakarta.persistence.EntityManager;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

    public PaymentRepository(EntityManager entityManager) {
        super(Payment.class, entityManager);
    }
}
