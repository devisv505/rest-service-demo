package com.devisv.rest.repository;

import com.devisv.rest.model.Model;

import java.util.List;
import java.util.UUID;

public interface Repository<E extends Model> {

    E getByUuid(UUID uuid);

    List<E> getAll();
}
