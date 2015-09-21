package com.hci.domain.base;

public class UuidId implements Id {
    private String uid;

    public UuidId(String uid) {
        this.uid = uid;
    }

    @Override
    public String getValue() {
        return uid;
    }

    @Override
    public Entity entityType() {
        return null;
    }
}
