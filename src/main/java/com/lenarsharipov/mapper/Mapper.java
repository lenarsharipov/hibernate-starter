package com.lenarsharipov.mapper;

public interface Mapper<F, T> {

    T mapFrom(F object);
}
