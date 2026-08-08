package com.warehouse.demo.service;

import java.util.List;

public interface AbstractService<T, ID> {
    List<T> readAll();
    T read(ID id);
    void delete(ID id);
}
