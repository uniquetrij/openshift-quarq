package com.infy.stg.quarq.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.infy.stg.quarq.domain.Person} entity.
 */
@RegisterForReflection
public class PersonDTO implements Serializable {
    
    public Long id;

    public String name;

    public Instant birth;

    public String status;

    public String country;

    public String city;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonDTO)) {
            return false;
        }

        return id != null && id.equals(((PersonDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", birth='" + birth + "'" +
            ", status='" + status + "'" +
            ", country='" + country + "'" +
            ", city='" + city + "'" +
            "}";
    }
}
