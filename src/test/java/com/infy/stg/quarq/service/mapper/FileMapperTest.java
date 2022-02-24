package com.infy.stg.quarq.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class FileMapperTest {

    private FileMapper fileMapper;

    @BeforeEach
    public void setUp() {
        fileMapper = new FileMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(fileMapper.fromId(id).id).isEqualTo(id);
        assertThat(fileMapper.fromId(null)).isNull();
    }
}
