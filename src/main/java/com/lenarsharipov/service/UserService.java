package com.lenarsharipov.service;

import com.lenarsharipov.dao.UserRepository;
import com.lenarsharipov.dto.UserCreateDto;
import com.lenarsharipov.dto.UserReadDto;
import com.lenarsharipov.entity.User;
import com.lenarsharipov.mapper.Mapper;
import com.lenarsharipov.mapper.UserCreateMapper;
import com.lenarsharipov.mapper.UserReadMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Long create(UserCreateDto userDto) {
        // validate
        // mapping
        User user = userCreateMapper.mapFrom(userDto);
        return userRepository.save(user).getId();
    }

    @Transactional
    public <T> Optional<T> findById(Long id, Mapper<User, T> mapper) {
        Map<String, Object> properties = Map.of(
                GraphSemantic.LOAD.getJakartaHintName(),
                userRepository.getEntityManager().createEntityGraph("WithCompany")
        );
        return userRepository.findById(id, properties)
                .map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Long id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public boolean delete(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        optionalUser.ifPresent(user -> userRepository.delete(user.getId()));
        return optionalUser.isPresent();
    }

}
