package com.hci.common.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import java.util.Date;
import java.util.Set;

public abstract class Entity<E extends Entity<E>> implements Validatable {
    private Id<E> id;

    @Min(0)
    private int version;

    private Date created;

    private Date updated;

    protected Validator validator;


    public Entity() {
        this.version = 0;
        this.created = new Date();
        this.updated = new Date();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Entity(Id<E> id, int version, Date created, Date updated) {
        this.id = id;
        this.version = version;
        this.created = created;
        this.updated = updated;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Entity id(final Id<E> uid) {
        this.id = uid;
        return this;
    }

    public Entity created(final Date created) {
        this.created = created;
        return this;
    }

    public Entity updated(final Date updated) {
        this.updated = updated;
        return this;
    }

    public Entity version(final int version) {
        this.version = version;
        return this;
    }

    public Id<E> getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Date getCreated() {
        return created;
    }

    public Date getUpdated() {
        return updated;
    }

    @Override
    public void validate() throws ConstraintViolationException {
        Set<ConstraintViolation<Entity<E>>> violations = this.validator.validate(this);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", id)
                .append("version", version)
                .append("updated", updated)
                .append("created", created)
                .toString();
    }
}
