package com.infy.stg.quarq.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.infy.stg.quarq.domain.enumeration.EncryptionAlgorithm;

/**
 * A DTO for the {@link com.infy.stg.quarq.domain.File} entity.
 */
@RegisterForReflection
public class FileDTO implements Serializable {
    
    public Long id;

    public String identifier;

    public String location;

    public String uri;

    public Boolean encryption;

    public EncryptionAlgorithm encryptionAlgorithm;

    public Set<EmployeeDTO> employees = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileDTO)) {
            return false;
        }

        return id != null && id.equals(((FileDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
            "id=" + id +
            ", identifier='" + identifier + "'" +
            ", location='" + location + "'" +
            ", uri='" + uri + "'" +
            ", encryption='" + encryption + "'" +
            ", encryptionAlgorithm='" + encryptionAlgorithm + "'" +
            ", employees='" + employees + "'" +
            "}";
    }
}
