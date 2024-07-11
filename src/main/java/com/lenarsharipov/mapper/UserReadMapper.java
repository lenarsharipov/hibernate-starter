package com.lenarsharipov.mapper;

import com.lenarsharipov.dto.UserReadDto;
import com.lenarsharipov.entity.User;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto mapFrom(User object) {
        return new UserReadDto(
                object.getId(),
                object.getPersonalInfo(),
                object.getUsername(),
                object.getRole(),
                Optional.ofNullable(object.getCompany())
                        .map(companyReadMapper::mapFrom)
                        .orElse(null)
        );
    }
}
