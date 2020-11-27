package com.epam.esm.service;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.converter.CertificateConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.service.utils.UpdatedCertificate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringConfig.class)
@SpringBootTest
@EnableAutoConfiguration
@ActiveProfiles("test")
class CertificateServiceImplTest {
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private TagService tagService;
    @SpyBean
    private CertificateConverter certificateConverter;
    @Mock
    private UpdatedCertificate updatedCertificate;
    @InjectMocks
    private CertificateService certificateService = new CertificateServiceImpl();
    private Pagination pagination;
    private ZonedDateTime date;
    private List<CertificateDto> dtoList;
    private List<Certificate> entityList;
    private CertificateDto certificateDto;
    private Certificate certificate;

    @BeforeEach
    void setUp() {
        date = ZonedDateTime.of(
                LocalDateTime.of(2020, 12, 7, 12, 12),
                ZoneId.of("+03:00")
        );
        pagination = new Pagination(100,0);
        certificateDto = CertificateDto.builder()
                .id(1)
                .name("Spa Certificate")
                .description("Very good spa")
                .duration(12)
                .lastUpdateDate(date)
                .createDate(date)
                .price(BigDecimal.valueOf(12.3))
                .tags(Arrays.asList(new TagDto(2, "#relax"), new TagDto(1, "#spa")))
                .build();
        certificate = new Certificate(1, "Spa Certificate", "Very good spa",
                BigDecimal.valueOf(12.3), date.toLocalDateTime(), date.getZone(),
                date.toLocalDateTime(), date.getZone(), 12,
                new HashSet<>(Arrays.asList(new Tag(1, "#spa", null)
                        , new Tag(2, "#relax", null))),
                null);
        dtoList = new ArrayList<CertificateDto>(){{
            add(certificateDto);
        }};
        entityList = new ArrayList<Certificate>(){{
            add(certificate);
        }};
    }

    @Test
    void findAll_thenReturnAllCertificatesList() {
        when(certificateRepository.findAll(pagination)).thenReturn(entityList);
        assertIterableEquals(dtoList, certificateService.findAll(pagination));
    }

    @Test
    void getById_whenCertificateExists_thenReturnCertificate() {
        when(certificateRepository.findById(1)).thenReturn(Optional.ofNullable(certificate));
        assertEquals(certificateDto, certificateService.findById(1));
    }

    @Test
    void getById_whenCertificateDoesNotExist_thenEntityNotFoundException() {
        when(certificateRepository.findById(9)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> certificateService.findById(9));
    }

    @Test
    void create_whenCertificateDoesNotExist_thenReturnCreatedCertificate() throws Exception {
        certificateDto.setTags(Collections.emptyList());
        certificate.setTags(Collections.emptySet());
        when(certificateRepository.getCertificate(certificate.getName(), certificate.getDescription()))
                .thenReturn(Optional.empty());
        when(certificateRepository.create(certificate)).thenReturn(Optional.of(certificate));
        doNothing().when(tagService).addTagsToCertificate(certificateDto.getTags(), certificate);

        assertEquals(certificateDto, certificateService.create(certificateDto));
    }

    @Test
    void create_whenCertificateIsAlreadyExist_thenEntityAlreadyExistsException() {
        when(certificateRepository.getCertificate(certificate.getName(), certificate.getDescription()))
                .thenReturn(Optional.ofNullable(certificate));
        assertThrows(EntityAlreadyExistsException.class, ()-> certificateService.create(certificateDto));
    }

    @Test
    void update_whenCertificateExists_thenReturnUpdatedCertificate() throws Exception {
        certificate.setTags(Collections.emptySet());
        certificateDto.setTags(Collections.emptyList());
        when(certificateRepository.findById(1)).thenReturn(Optional.of(certificate));
        when(updatedCertificate.getUpdatedCertificate(certificateDto, certificateDto)).thenReturn(certificateDto);
        when(certificateRepository.update(certificate)).thenReturn(Optional.of(certificate));
        doNothing().when(tagService).addTagsToCertificate(certificateDto.getTags(), certificate);

        assertEquals(certificateDto, certificateService.update(certificateDto, 1));
    }

    @Test
    void update_whenCertificateIsNotNullAndDoesNotExist_thenEntityNotFoundException() {
        when(certificateRepository.findById(9)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> certificateService.update(certificateDto, 9));
    }

    @Test
    void getFilteredCertificates_whenFilteredParamsAreEmpty_thenReturnAllCertificatesList() {
        when(certificateRepository.getFilteredCertificates(Collections.emptyMap(), pagination)).thenReturn(entityList);
        assertIterableEquals(dtoList, certificateService.getFilteredCertificates(Collections.emptyMap(), pagination));
    }

    @Test
    void getFilteredCertificates_whenFilteredParamsAreIllegal_thenReturnAllCertificatesList() {
        Map<String, String> filteredParams = new HashMap<String, String>(){{
            put("illegal", "illegal");
        }};

        when(certificateRepository.getFilteredCertificates(filteredParams, pagination)).thenReturn(entityList);
        assertIterableEquals(dtoList, certificateService.getFilteredCertificates(filteredParams, pagination));
    }

    @Test
    void getFilteredCertificates_whenFilteredParamsAreLegal_thenReturnFilteredCertificatesList() {
        Map<String, String> filteredParams = new HashMap<String, String>(){{
            put("tag", "spa");
        }};

        when(certificateRepository.getFilteredCertificates(filteredParams, pagination)).thenReturn(entityList);
        assertIterableEquals(dtoList,certificateService.getFilteredCertificates(filteredParams, pagination));
    }
}