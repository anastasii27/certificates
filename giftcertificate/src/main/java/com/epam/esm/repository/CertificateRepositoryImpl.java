package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("certificateRepository")
public class CertificateRepositoryImpl implements CertificateRepository {
    private static final String ALL_CERTIFICATES = "SELECT c FROM Certificate c";
    private static final String GET_CERTIFICATE = "SELECT c FROM Certificate c WHERE c.name=?1 AND c.description=?2";
    private static final String GET_USER_CERTIFICATES = "SELECT c FROM Certificate c JOIN c.orders o WHERE o.userId=?1";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Certificate> findAll(Pagination pagination) {
        return entityManager.createQuery(ALL_CERTIFICATES, Certificate.class)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .getResultList();
    }

    @Override
    public Optional<Certificate> findById(long id) {
        return Optional.ofNullable(
                entityManager.find(Certificate.class, id)
        );
    }

    @Override
    public void delete(long id) {
        Certificate certificate = entityManager.find(Certificate.class, id);
        entityManager.remove(certificate);
    }

    @Override
    public Optional<Certificate> create(Certificate certificate) {
        certificate.setTags(null);
        entityManager.persist(certificate);

        return findById(certificate.getId());
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) {
        certificate.setTags(null);
        return Optional.ofNullable(
               entityManager.merge(certificate)
        );
    }

    @Override
    public List<Certificate> getFilteredCertificates(Map<String, String> filterParams, Pagination pagination) {
        SelectFilteredQuery query =  new SelectFilteredQuery();
        TypedQuery<Certificate> typedQuery = entityManager.createQuery(
                query.createQuery(filterParams), Certificate.class);

        filterParams.forEach(typedQuery::setParameter);

        return typedQuery.setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .getResultList();
    }

    @Override
    public Optional<Certificate> getCertificate(String name, String description) {
         Certificate  certificate = entityManager.createQuery(GET_CERTIFICATE, Certificate.class)
                .setParameter(1, name)
                .setParameter(2, description)
                .getResultList().stream().findFirst().orElse(null);

         return Optional.ofNullable(certificate);
    }

    @Override
    public List<Certificate> getUserCertificates(long userId) {
        return entityManager
                .createQuery(GET_USER_CERTIFICATES, Certificate.class)
                .setParameter(1, userId)
                .getResultList();
    }
}


