package com.hci.common;

import com.hci.domain.base.Entity;
import com.hci.domain.base.Id;

public interface Repository<E extends Entity> {
    E get(Id<E> id);
}
