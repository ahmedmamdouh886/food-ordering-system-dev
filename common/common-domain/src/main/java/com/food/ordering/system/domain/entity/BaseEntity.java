package com.food.ordering.system.domain.entity;

import java.util.Objects;

/**
 * I will use this abstract class for creating entities.
 *
 * @param <ID>
 */
public abstract class BaseEntity<ID> {
    // Remember, for entities, we should have a unique identifier, so that's why I use this unique ID in the Equals and HashCode methods.
    // We will have another id corresponding to our child class type, e.g. productId and so on.
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
