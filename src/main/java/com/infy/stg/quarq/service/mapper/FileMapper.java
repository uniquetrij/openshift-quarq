package com.infy.stg.quarq.service.mapper;


import com.infy.stg.quarq.domain.*;
import com.infy.stg.quarq.service.dto.FileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link File} and its DTO {@link FileDTO}.
 */
@Mapper(componentModel = "cdi", uses = {EmployeeMapper.class})
public interface FileMapper extends EntityMapper<FileDTO, File> {



    default File fromId(Long id) {
        if (id == null) {
            return null;
        }
        File file = new File();
        file.id = id;
        return file;
    }
}
