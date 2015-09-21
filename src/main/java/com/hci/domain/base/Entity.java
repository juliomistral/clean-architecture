package com.hci.domain.base;

import javax.validation.constraints.Min;
import java.util.Date;

public class Entity<T extends Entity> {
    private Id<T> id;
    @Min(0) private int version;
    private Date created;
    private Date updated;


    public Entity() {
        this.version = 0;
        this.created = new Date();
    }
    
    public Entity id(final Id<T> uid) {
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

    public Id<T> getId() {
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
}
