package com.infy.stg.quarq.service.mapper;


import com.infy.stg.quarq.domain.*;
import com.infy.stg.quarq.service.dto.PersonDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Person} and its DTO {@link PersonDTO}.
 */
@Mapper(componentModel = "cdi", uses = {})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {



    default Person fromId(Long id) {
        if (id == null) {
            return null;
        }
        Person person = new Person();
        person.id = id;
        return person;
    }
}
