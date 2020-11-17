package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository("certificateRepository")
public class CertificateRepositoryImpl implements CertificateRepository {
    private static final String ALL_CERTIFICATES = "SELECT c FROM Certificate c";
    private static final String GET_CERTIFICATE = "SELECT c FROM Certificate c WHERE c.name=?1 AND c.description=?2";
    private static final String GET_USER_CERTIFICATES = "SELECT c FROM Certificate c JOIN c.orders o WHERE o.userId=?1";
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public List<Certificate> findAll(Pagination pagination) {
        return sessionFactory.getCurrentSession()
                .createQuery(ALL_CERTIFICATES, Certificate.class)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .list();
    }

    @Override
    public Optional<Certificate> findById(long id) {
        return Optional.ofNullable(
                sessionFactory.getCurrentSession().get(Certificate.class, id)
        );
    }

    @Override
    public void delete(long id) {
        Session session = sessionFactory.getCurrentSession();

        Certificate certificate = session.get(Certificate.class, id);
        session.delete(certificate);
    }

    @Override
    public Optional<Certificate> create(Certificate certificate) {
        certificate.setTags(null);
        long id = (Long) sessionFactory
                .getCurrentSession()
                .save(certificate);
        return findById(id);
    }

    @Override
    public Optional<Certificate> update(Certificate certificate) {
        certificate.setTags(null);
        return Optional.ofNullable(
                (Certificate) sessionFactory.getCurrentSession().merge(certificate)
        );
    }

    @Override
    public List<Certificate> getFilteredCertificates(Map<String, String> filterParams, Pagination pagination) {
        SelectFilteredQuery query =  new SelectFilteredQuery();
        return sessionFactory.getCurrentSession()
                .createQuery(query.createQuery(filterParams), Certificate.class)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .list();
    }

    @Override
    public Optional<Certificate> getCertificate(String name, String description) {
         Certificate  certificate = sessionFactory.getCurrentSession().createQuery(GET_CERTIFICATE, Certificate.class)
                .setParameter(1, name)
                .setParameter(2, description)
                .getResultList().stream().findFirst().orElse(null);
         return Optional.ofNullable(certificate);
    }

    @Override
    public List<Certificate> getUserCertificates(long userId) {
        return sessionFactory.getCurrentSession()
                .createQuery(GET_USER_CERTIFICATES, Certificate.class)
                .setParameter(1, userId)
                .list();
    }
}


