package com.lenarsharipov.dao;

import com.lenarsharipov.entity.Company;
import jakarta.persistence.EntityManager;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

    public CompanyRepository(EntityManager entityManager) {
        super(Company.class, entityManager);
    }
}
