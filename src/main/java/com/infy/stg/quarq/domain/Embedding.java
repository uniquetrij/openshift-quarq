package com.infy.stg.quarq.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

/**
 * A Embedding.
 */
@Entity
@Table(name = "embedding")
@RegisterForReflection
public class Embedding extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Lob
    @Column(name = "embedding")
    public byte[] embedding;

    @Column(name = "embedding_content_type")
      public String embeddingContentType;

    @ManyToMany
    @JoinTable(name = "embedding_employee",
               joinColumns = @JoinColumn(name = "embedding_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
    @JsonbTransient
    public Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Embedding)) {
            return false;
        }
        return id != null && id.equals(((Embedding) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Embedding{" +
            "id=" + id +
            ", embedding='" + embedding + "'" +
            //", embeddingContentType='" + embeddingContentType() + "'" +
            "}";
    }

    public Embedding update() {
        return update(this);
    }

    public Embedding persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Embedding update(Embedding embedding) {
        if (embedding == null) {
            throw new IllegalArgumentException("embedding can't be null");
        }
        var entity = Embedding.<Embedding>findById(embedding.id);
        if (entity != null) {
            entity.embedding = embedding.embedding;
            entity.employees = embedding.employees;
        }
        return entity;
    }

    public static Embedding persistOrUpdate(Embedding embedding) {
        if (embedding == null) {
            throw new IllegalArgumentException("embedding can't be null");
        }
        if (embedding.id == null) {
            persist(embedding);
            return embedding;
        } else {
            return update(embedding);
        }
    }

    public static PanacheQuery<Embedding> findAllWithEagerRelationships() {
        return find("select distinct embedding from Embedding embedding left join fetch embedding.employees");
    }

    public static Optional<Embedding> findOneWithEagerRelationships(Long id) {
        return find("select embedding from Embedding embedding left join fetch embedding.employees where embedding.id =?1", id).firstResultOptional();
    }

}
