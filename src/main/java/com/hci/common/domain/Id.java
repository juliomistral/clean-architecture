package com.hci.common.domain;

/**
 * Created by juliomistral on 9/20/15.
 */
public interface Id<E extends Entity<E>> {
    String getValue();
    Class<E> entityType();
}
