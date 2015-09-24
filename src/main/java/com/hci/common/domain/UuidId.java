package com.hci.common.domain;

public class UuidId <E extends Entity<E>> implements Id<E> {
    private String uid;
    private Class<E> clazz;

    public UuidId(String uid, Class<E> clazz) {
        this.uid = uid;
        this.clazz = clazz;
    }

    @Override
    public String getValue() {
        return uid;
    }

    @Override
    public Class<E> entityType() {
        return this.clazz;
    }

    @Override
    public String toString() {
        return String.format("[ %s : %s ]", clazz.getSimpleName(), uid);
    }
}
