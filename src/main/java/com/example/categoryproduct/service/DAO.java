package com.example.categoryproduct.service;

import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    Optional<T> get(Long id);
    List<T> getAll();
    int save(T object);
    Optional<T> update(long id, T object);
    int delete(long id);
}
