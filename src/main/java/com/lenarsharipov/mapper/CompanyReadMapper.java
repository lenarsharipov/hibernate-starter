package com.lenarsharipov.mapper;

import com.lenarsharipov.dto.CompanyReadDto;
import com.lenarsharipov.entity.Company;
import org.hibernate.Hibernate;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {

    @Override
    public CompanyReadDto mapFrom(Company object) {
        /*
        Нам надо проинициализировать Map,
        потому что это Hibernate PersistentMap.
        Если не проинициализировать мапу, мы
        можем получить LazyInitializationException
         */
        Hibernate.initialize(object.getLocales());
        return new CompanyReadDto(
                object.getId(),
                object.getName(),
                object.getLocales()
        );
    }
}
