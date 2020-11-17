package com.epam.esm.repository;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@SpringJUnitConfig(SpringConfig.class)
@EnableAutoConfiguration
@ActiveProfiles("test")
class CertificateRepositoryImplTest {
    private Pagination pagination;
    private List<Certificate> certificates;
    private Certificate certificate1;
    private Certificate certificate2;
    private ZonedDateTime date;
    @Autowired
    private CertificateRepository certificateRepository;

    @BeforeEach
    void setUp() {
        pagination = new Pagination(2,0);
        date = ZonedDateTime.parse("2020-09-12T12:12:11+03:00");
        certificate1 = new Certificate(1, "Bike Certificate", "Very good bikes",
                BigDecimal.valueOf(12.99), date.toLocalDateTime(), date.getZone(),
                date.toLocalDateTime(), date.getZone(), 12,
                new HashSet<>(Collections.singletonList(new Tag(1, "velo", null))),
                null);
        certificate2 = new Certificate(1, "Spa Certificate", "Great spa",
                BigDecimal.valueOf(19.99), date.toLocalDateTime(), date.getZone(),
                date.toLocalDateTime(), date.getZone(), 22,
                new HashSet<>(Collections.singletonList(new Tag(2, "spa", null))),
                null);
        certificates = new ArrayList<Certificate>() {{
            add(certificate1);
            add(certificate2);
        }};
    }

    @Test
    void findAll_whenCertificatesExists_thenReturnList() {
        assertIterableEquals(certificates, certificateRepository.findAll(pagination));
    }

    @Test
    void getById_whenCertificateExists_thenReturnOptionalCertificate() {
        assertEquals(Optional.ofNullable(certificate1), certificateRepository.findById(1));
    }

    @Test
    void getById_whenCertificateDoesNotExist_thenReturnEmptyOptional() {
        assertEquals(Optional.empty(), certificateRepository.findById(99));
    }

    @Test
    void delete_whenCertificateExists_thenOneRowIsRemovedFromDataBase() {
        certificateRepository.delete(1);
        certificates.remove(certificate1);

        assertIterableEquals(certificates, certificateRepository.findAll(pagination));
    }

    @Test
    void delete_whenCertificateDoesNotExist_thenNoRowsAreRemovedFromDataBase() {
        certificateRepository.delete(3);

        assertIterableEquals(certificates, certificateRepository.findAll(pagination));
    }

    @Test
    void create_whenCertificateIsNotNull_thenReturnOptionalCertificate() {
        certificate1.setId(3);
        certificate1.setTags(Collections.emptySet());

        Optional<Certificate> expected = Optional.of(certificate1);
        Optional<Certificate> actual = certificateRepository.create(certificate1);
        assertEquals(expected, actual);
    }

    @Test
    void create_whenCertificateIsNull_thenNullPointerException() {
        assertThrows(NullPointerException.class, ()-> certificateRepository.create(null));
    }

    @Test
    void update_whenCertificateExists_thenReturnOptionalCertificate() {
        Optional<Certificate> expected = Optional.of(certificate1);
        Optional<Certificate> actual = certificateRepository.update(certificate1);

        assertEquals(expected, actual);
    }

    @Test
    void update_whenCertificateDoesNotExist_thenReturnEmptyOptional() {
        certificate1.setId(3);
        assertEquals(Optional.empty(), certificateRepository.update(certificate1));
    }

    @Test
    void update_whenCertificateIsNull_thenNullPointerException() {
        assertThrows(NullPointerException.class, ()-> certificateRepository.update(null));
    }

    @Test
    void getFilteredCertificates_whenMapIsEmpty_thenReturnAllCertificatesList(){
        assertIterableEquals(certificates,
                certificateRepository.getFilteredCertificates(Collections.emptyMap(), pagination));
    }

    @Test
    void getFilteredCertificates_whenMapIsNull_thenNullPointerException(){
        assertThrows(NullPointerException.class,
                ()-> certificateRepository.getFilteredCertificates(null, pagination));
    }

    @Test
    void getFilteredCertificates_whenMapContainsIllegalFilterParams_thenIllegalArgumentException(){
        Map<String, String> filteredParams = new HashMap<String, String>(){{
            put("illegal", "illegal");
        }};

        assertThrows(IllegalArgumentException.class,
                ()-> certificateRepository.getFilteredCertificates(filteredParams, pagination));
    }

    @Test
    void getFilteredCertificates_whenMapContainsLegalFilterParams_thenReturnFilteredCertificatesList(){
        Map<String, String> filteredParams = new HashMap<String, String>(){{
            put("tag", "spa");
        }};

        assertIterableEquals(Collections.singletonList(certificate2),
                             certificateRepository.getFilteredCertificates(filteredParams, pagination));
    }

    @Test
    void getCertificate_whenCertificateExists_thenId(){
       assertEquals(certificate1, certificateRepository.getCertificate(certificate1.getName(), certificate1.getDescription()));
    }

    @Test
    void getCertificate_whenCertificateDoesNotExist_thenZero(){
        certificate1.setName("new name");
        assertEquals(Optional.empty(), certificateRepository.getCertificate(certificate1.getName(), certificate1.getDescription()));
    }

    @Test
    void getCertificate_whenCertificateNameAndDescriptionAreNull_thenNullPointerException(){
        assertThrows(NullPointerException.class, ()-> certificateRepository.getCertificate(null, null));
    }
}

