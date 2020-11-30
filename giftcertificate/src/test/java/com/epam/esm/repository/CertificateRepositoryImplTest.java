package com.epam.esm.repository;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringJUnitConfig(SpringConfig.class)
@EnableAutoConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Transactional
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
        certificate2 = new Certificate(2, "Spa Certificate", "Great spa",
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
    void delete_whenCertificateDoesExists_thenDoNothing() {
        certificateRepository.delete(2);
        assertIterableEquals(certificates, certificateRepository.findAll(pagination));
    }

    @Test
    void create_whenCertificateIsNotNull_thenReturnOptionalCertificate() {
        Optional<Certificate> expected = Optional.of(certificate2);
        certificate1.setId(3);
        Optional<Certificate> actual = certificateRepository.create(certificate2);
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
    void update_whenCertificateDoesNotExist_thenReturnNewCertificate() {
        Certificate certificate  = new Certificate(3, "New one", "Very good bikes",
                BigDecimal.valueOf(12.99), date.toLocalDateTime(), date.getZone(),
                date.toLocalDateTime(), date.getZone(), 12,
                new HashSet<>(Collections.singletonList(new Tag(1, "velo", null))),
                null);

        assertEquals(Optional.of(certificate), certificateRepository.update(certificate));
    }

    @Test
    void update_whenCertificateIsNull_thenNullPointerException() {
        assertThrows(NullPointerException.class, ()-> certificateRepository.update(null));
    }

    @Test
    void getFilteredCertificates_whenMapIsNull_thenNullPointerException(){
        assertThrows(NullPointerException.class,
                ()-> certificateRepository.getFilteredCertificates(null, pagination));
    }

    @Test
    void getCertificate_whenCertificateExists_thenOptionalCertificate(){
       assertEquals(Optional.ofNullable(certificate1),
               certificateRepository.getCertificate(certificate1.getName(), certificate1.getDescription()));
    }

    @Test
    void getCertificate_whenCertificateDoesNotExist_thenEmptyOptional(){
        certificate1.setName("new name");
        assertEquals(Optional.empty(),
                certificateRepository.getCertificate(certificate1.getName(), certificate1.getDescription()));
    }

    @Test
    void getCertificate_whenCertificateNameAndDescriptionAreNull_thenEmptyOptional(){
        assertEquals(Optional.empty(), certificateRepository.getCertificate(null, null));
    }
}

