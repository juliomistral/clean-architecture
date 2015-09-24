package com.hci.common.dao;

import com.hci.common.domain.Entity;
import com.hci.common.domain.Id;

public interface Repository<E extends Entity<E>> {
    E get(Id<E> id);
    Id<E> create(E entity);
}
