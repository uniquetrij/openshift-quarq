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

import com.infy.stg.quarq.domain.enumeration.EncryptionAlgorithm;

/**
 * A File.
 */
@Entity
@Table(name = "file")
@RegisterForReflection
public class File extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Column(name = "identifier")
    public String identifier;

    @Column(name = "location")
    public String location;

    @Column(name = "uri")
    public String uri;

    @Column(name = "encryption")
    public Boolean encryption;

    @Enumerated(EnumType.STRING)
    @Column(name = "encryption_algorithm")
    public EncryptionAlgorithm encryptionAlgorithm;

    @ManyToMany
    @JoinTable(name = "file_employee",
               joinColumns = @JoinColumn(name = "file_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"))
    @JsonbTransient
    public Set<Employee> employees = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof File)) {
            return false;
        }
        return id != null && id.equals(((File) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "File{" +
            "id=" + id +
            ", identifier='" + identifier + "'" +
            ", location='" + location + "'" +
            ", uri='" + uri + "'" +
            ", encryption='" + encryption + "'" +
            ", encryptionAlgorithm='" + encryptionAlgorithm + "'" +
            "}";
    }

    public File update() {
        return update(this);
    }

    public File persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static File update(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file can't be null");
        }
        var entity = File.<File>findById(file.id);
        if (entity != null) {
            entity.identifier = file.identifier;
            entity.location = file.location;
            entity.uri = file.uri;
            entity.encryption = file.encryption;
            entity.encryptionAlgorithm = file.encryptionAlgorithm;
            entity.employees = file.employees;
        }
        return entity;
    }

    public static File persistOrUpdate(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file can't be null");
        }
        if (file.id == null) {
            persist(file);
            return file;
        } else {
            return update(file);
        }
    }

    public static PanacheQuery<File> findAllWithEagerRelationships() {
        return find("select distinct file from File file left join fetch file.employees");
    }

    public static Optional<File> findOneWithEagerRelationships(Long id) {
        return find("select file from File file left join fetch file.employees where file.id =?1", id).firstResultOptional();
    }

}
