package com.infy.stg.quarq.service.impl;

import com.infy.stg.quarq.service.FileService;
import com.infy.stg.quarq.domain.File;
import com.infy.stg.quarq.service.dto.FileDTO;
import com.infy.stg.quarq.service.mapper.FileMapper;
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
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    @Inject
    FileMapper fileMapper;

    @Override
    @Transactional
    public FileDTO persistOrUpdate(FileDTO fileDTO) {
        log.debug("Request to save File : {}", fileDTO);
        var file = fileMapper.toEntity(fileDTO);
        file = File.persistOrUpdate(file);
        return fileMapper.toDto(file);
    }

    /**
     * Delete the File by id.
     *
     * @param id the id of the entity.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        File.findByIdOptional(id).ifPresent(file -> {
            file.delete();
        });
    }

    /**
     * Get one file by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    public Optional<FileDTO> findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return File.findOneWithEagerRelationships(id)
            .map(file -> fileMapper.toDto((File) file));
    }

    /**
     * Get all the files.
     * @return the list of entities.
     */
    @Override
    public  List<FileDTO> findAll() {
        log.debug("Request to get all Files");
        List<File> files = File.findAllWithEagerRelationships().list();
        return fileMapper.toDto(files);
    }


    /**
     * Get all the files with eager load of many-to-many relationships.
     * @return the list of entities.
     */
    public  List<FileDTO> findAllWithEagerRelationships() {
        List<File> files = File.findAllWithEagerRelationships().list();
        return fileMapper.toDto(files);
    }


}
