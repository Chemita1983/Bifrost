package com.bifrost.aplication.service;

import com.bifrost.aplication.domain.BifrostEntity;

import java.util.List;

public interface BifrostService <T extends BifrostEntity> {

    List<T> readAll();

    List<T> readByName(String name);

    T add(T t);

}
