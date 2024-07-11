package com.lenarsharipov.dto;

import com.lenarsharipov.entity.PersonalInfo;
import com.lenarsharipov.entity.Role;

public record UserCreateDto(PersonalInfo personalInfo,
                            String username,
                            Role role,
                            Integer companyId) {
}
