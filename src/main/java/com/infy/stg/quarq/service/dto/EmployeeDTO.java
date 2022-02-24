package com.infy.stg.quarq.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.infy.stg.quarq.domain.Employee} entity.
 */
@RegisterForReflection
public class EmployeeDTO implements Serializable {
    
    public Long id;

    public String identifier;

    public String name;

    public String encryptionKey;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmployeeDTO)) {
            return false;
        }

        return id != null && id.equals(((EmployeeDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
            "id=" + id +
            ", identifier='" + identifier + "'" +
            ", name='" + name + "'" +
            ", encryptionKey='" + encryptionKey + "'" +
            "}";
    }
}
