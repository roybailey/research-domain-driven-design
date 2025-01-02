package me.roybailey.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;


/**
 * Base class to signify the class should be an identifiable Entity Object.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class DomainEntity {

    private String id = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomainEntity that = (DomainEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
