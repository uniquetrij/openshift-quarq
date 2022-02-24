package com.infy.stg.quarq.service.impl;

import com.infy.stg.quarq.service.PersonService;
import com.infy.stg.quarq.domain.Person;
import com.infy.stg.quarq.service.dto.PersonDTO;
import com.infy.stg.quarq.service.mapper.PersonMapper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional
public class PersonServiceImpl implements PersonService {

    private final Logger log = LoggerFactory.getLogger(PersonServiceImpl.class);

    @Inject
    PersonMapper personMapper;

    @Override
    @Transactional
    public PersonDTO persistOrUpdate(PersonDTO personDTO) {
        log.debug("Request to save Person : {}", personDTO);
        var person = personMapper.toEntity(personDTO);
        person = Person.persistOrUpdate(person);
        return personMapper.toDto(person);
    }

    /**
     * Delete the Person by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete Person : {}", id);
        Person.findByIdOptional(id).ifPresent(person -> {
            person.delete();
        });
    }

    /**
     * Get one person by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<PersonDTO> findOne(Long id) {
        log.debug("Request to get Person : {}", id);
        return Person.findByIdOptional(id)
            .map(person -> personMapper.toDto((Person) person)); 
    }

    /**
     * Get all the people.
     * @return the list of entities.
     */
    @Override
    public  List<PersonDTO> findAll() {
        log.debug("Request to get all People");
        List<Person> people = Person.findAll().list();
        return personMapper.toDto(people);
    }



}
