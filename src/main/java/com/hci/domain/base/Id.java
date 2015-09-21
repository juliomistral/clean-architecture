package com.hci.domain.base;

/**
 * Created by juliomistral on 9/20/15.
 */
public interface Id<E extends Entity> {
    String getValue();
    E entityType();


}
