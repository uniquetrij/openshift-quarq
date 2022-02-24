package com.infy.stg.quarq.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Employee.
 */
@Entity
@Table(name = "employee")
@RegisterForReflection
public class Employee extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Column(name = "identifier")
    public String identifier;

    @Column(name = "name")
    public String name;

    @Column(name = "encryption_key")
    public String encryptionKey;

    @ManyToMany(mappedBy = "employees")
    @JsonbTransient
    public Set<Embedding> embeddings = new HashSet<>();

    @ManyToMany(mappedBy = "employees")
    @JsonbTransient
    public Set<File> files = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Employee)) {
            return false;
        }
        return id != null && id.equals(((Employee) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Employee{" +
            "id=" + id +
            ", identifier='" + identifier + "'" +
            ", name='" + name + "'" +
            ", encryptionKey='" + encryptionKey + "'" +
            "}";
    }

    public Employee update() {
        return update(this);
    }

    public Employee persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Employee update(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("employee can't be null");
        }
        var entity = Employee.<Employee>findById(employee.id);
        if (entity != null) {
            entity.identifier = employee.identifier;
            entity.name = employee.name;
            entity.encryptionKey = employee.encryptionKey;
            entity.embeddings = employee.embeddings;
            entity.files = employee.files;
        }
        return entity;
    }

    public static Employee persistOrUpdate(Employee employee) {
        if (employee == null) {
            throw new IllegalArgumentException("employee can't be null");
        }
        if (employee.id == null) {
            persist(employee);
            return employee;
        } else {
            return update(employee);
        }
    }


}
