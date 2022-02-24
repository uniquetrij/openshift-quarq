package com.infy.stg.quarq.service.dto;


import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.infy.stg.quarq.domain.Embedding} entity.
 */
@RegisterForReflection
public class EmbeddingDTO implements Serializable {
    
    public Long id;

    @Lob
    public byte[] embedding;

    public String embeddingContentType;

    public Set<EmployeeDTO> employees = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EmbeddingDTO)) {
            return false;
        }

        return id != null && id.equals(((EmbeddingDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "EmbeddingDTO{" +
            "id=" + id +
            ", embedding='" + embedding + "'" +
            ", employees='" + employees + "'" +
            "}";
    }
}
