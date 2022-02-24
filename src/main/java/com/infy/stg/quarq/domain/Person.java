package com.infy.stg.quarq.domain;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import javax.json.bind.annotation.JsonbTransient;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Person.
 */
@Entity
@Table(name = "person")
@RegisterForReflection
public class Person extends PanacheEntityBase implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    public Long id;

    @Column(name = "name")
    public String name;

    @Column(name = "birth")
    public Instant birth;

    @Column(name = "status")
    public String status;

    @Column(name = "country")
    public String country;

    @Column(name = "city")
    public String city;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Person)) {
            return false;
        }
        return id != null && id.equals(((Person) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Person{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", birth='" + birth + "'" +
            ", status='" + status + "'" +
            ", country='" + country + "'" +
            ", city='" + city + "'" +
            "}";
    }

    public Person update() {
        return update(this);
    }

    public Person persistOrUpdate() {
        return persistOrUpdate(this);
    }

    public static Person update(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("person can't be null");
        }
        var entity = Person.<Person>findById(person.id);
        if (entity != null) {
            entity.name = person.name;
            entity.birth = person.birth;
            entity.status = person.status;
            entity.country = person.country;
            entity.city = person.city;
        }
        return entity;
    }

    public static Person persistOrUpdate(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("person can't be null");
        }
        if (person.id == null) {
            persist(person);
            return person;
        } else {
            return update(person);
        }
    }


}
