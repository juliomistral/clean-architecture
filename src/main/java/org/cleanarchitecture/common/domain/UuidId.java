package org.cleanarchitecture.common.domain;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UuidId<?> uuidId = (UuidId<?>) o;
        if (!uid.equals(uuidId.uid)) return false;
        return clazz.equals(uuidId.clazz);
    }

    @Override
    public int hashCode() {
        int result = uid.hashCode();
        result = 31 * result + clazz.hashCode();
        return result;
    }
}
