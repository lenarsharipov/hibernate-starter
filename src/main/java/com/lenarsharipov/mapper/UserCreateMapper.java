package com.lenarsharipov.mapper;

import com.lenarsharipov.dao.CompanyRepository;
import com.lenarsharipov.dto.UserCreateDto;
import com.lenarsharipov.entity.User;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDto object) {
        return User.builder()
                .personalInfo(object.personalInfo())
                .username(object.username())
                .role(object.role())
                .company(companyRepository.findById(object.companyId()).orElseThrow(null))
                .build();
    }
}
