package com.epam.esm.service;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(SpringConfig.class)
@SpringBootTest
@EnableConfigurationProperties
@ActiveProfiles("test")
class TagServiceImplTest {
    private List<TagDto> dtoTags;
    private List<Tag> entityTags;
    private TagDto tagDto1;
    private TagDto tagDto2;
    private Tag tagEntity1;
    private Tag tagEntity2;
    private Pagination pagination;
    @MockBean
    private TagRepository tagRepository;
    @SpyBean
    private TagConverter tagConverter;
    @InjectMocks
    private TagService tagService = new TagServiceImpl();

    @BeforeEach
    void setUp() {
        pagination = new Pagination(100, 0);
        MockitoAnnotations.initMocks(this);
        tagDto1 = new TagDto(1, "velo");
        tagDto2 = new TagDto(2, "spa");
        dtoTags = new ArrayList<TagDto>(){{
            add(tagDto1);
            add(tagDto2);
        }};
        tagEntity1 = new Tag(1, "velo", null);
        tagEntity2 = new Tag(2, "spa", null);
        entityTags = new ArrayList<Tag>(){{
            add(tagEntity1);
            add(tagEntity2);
        }};
    }

    @Test
    void create_whenTagIsNotNullAndDoesNotExist_thenCreatedTag() {
        when(tagRepository.create(tagEntity1)).thenReturn(Optional.of(tagEntity1));
        assertEquals(tagDto1, tagService.create(tagDto1));
    }

    @Test
    void create_whenTagIsNull_thenIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, ()->tagService.create(null));
    }

    @Test
    void findAll_thenReturnAllTagsList() {
        when(tagRepository.findAll(pagination)).thenReturn(entityTags);
        assertIterableEquals(dtoTags, tagService.findAll(pagination));
    }

    @Test
    void findById_whenTagExists_thenReturnTag() {
        when(tagRepository.findById(1)).thenReturn(Optional.ofNullable(tagEntity1));
        assertEquals(tagService.findById(1), tagDto1);
    }

    @Test
    void findById_whenTagDoesNotExist_thenEntityNotFoundException() {
        when(tagRepository.findById(9)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> tagService.findById(9));
    }

    @Test
    void getCertificateTags_whenCertificateExists_thenTagList() {
        when(tagRepository.getCertificateTags(9)).thenReturn(entityTags);
        assertIterableEquals(entityTags, tagService.getCertificateTags(9));
    }

    @Test
    void getCertificateTags_whenCertificateDoesNotExist_thenEmptyList() {
        when(tagRepository.getCertificateTags(10)).thenReturn(Collections.emptyList());
        assertIterableEquals(Collections.emptyList(), tagService.getCertificateTags(10));
    }

    @Test
    void delete_whenTagDoesNotExist_thenEntityNotFoundException() {
        when(tagRepository.findById(9)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> tagService.delete(9));
    }

    @Test
    void delete_whenTagExists_thenDoNothing() {
        when(tagRepository.findById(9)).thenReturn(Optional.ofNullable(tagEntity1));
        doNothing().when(tagRepository).delete(9);
    }
}