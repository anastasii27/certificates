package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository("tagRepository")
public class TagRepositoryImpl implements TagRepository {
    private static final String TAGS_LIST  = "SELECT t FROM Tag t";
    private static final String ADD_TAGS_TO_CERTIFICATE = "INSERT INTO `gift-certificates`.`tag_m2m_gift_certificate`\n" +
            "VALUES((SELECT id FROM `gift-certificates`.tag WHERE `name` = ?), ?);";
    private static final String DELETE_GIFT_CERTIFICATE_TAGS = "DELETE FROM `gift-certificates`.`tag_m2m_gift_certificate`\n" +
            "WHERE `giftCertificateId` = ? AND tagId = (SELECT id FROM tag WHERE `name`= ?)";
    private static final String CERTIFICATE_TAGS = "SELECT `id`, `name` FROM `gift-certificates`.`tag_m2m_gift_certificate`\n" +
            "JOIN tag ON `tag_m2m_gift_certificate`.`tagId` = tag.id\n" +
            "WHERE `giftCertificateId`=?;";
   @PersistenceContext
   private EntityManager entityManager;

    @Override
    public void createAll(List<Tag> tags) {
        for (Tag tag: tags) {
            entityManager.persist(tag);
        }
    }

    @Override
    public void addTagsToCertificate(List<Tag> tags, long certificateId) {
        for (Tag tag: tags) {
            entityManager.createNativeQuery(ADD_TAGS_TO_CERTIFICATE)
                    .setParameter(1, tag.getName())
                    .setParameter(2, certificateId)
                    .executeUpdate();
        }
    }

    @Override
    public void deleteCertificateTags(List<Tag> tags, long certificateId) {
        for (Tag tag: tags) {
            entityManager.createNativeQuery(DELETE_GIFT_CERTIFICATE_TAGS)
                    .setParameter(1, certificateId)
                    .setParameter(2, tag.getName())
                    .executeUpdate();
        }
    }

    @Override
    public List<Tag> getCertificateTags(long certificateId) {
        return entityManager.createNativeQuery(CERTIFICATE_TAGS, Tag.class)
                .setParameter(1,certificateId)
                .getResultList();
    }

    @Override
    public Optional<Tag> findById(long id) {
        return Optional.ofNullable(
                entityManager.find(Tag.class, id)
        );
    }

    @Override
    public List<Tag> findAll(Pagination pagination) {
        return entityManager.createQuery(TAGS_LIST, Tag.class)
                .setFirstResult(pagination.getOffset())
                .setMaxResults(pagination.getLimit())
                .getResultList();
    }

    @Override
    public Optional<Tag> create(Tag tag) {
        entityManager.persist(tag);
        return findById(tag.getId());
    }

    @Override
    public void delete(long id) {
        Tag tag = entityManager.find(Tag.class, id);
        entityManager.remove(tag);
    }

    @Override
    public List<Tag> getMostUsedTags(List<Certificate> certificates) {
        SelectMostUsedTagsQuery mostUsedTagsQuery = new SelectMostUsedTagsQuery();
        return entityManager
                .createNativeQuery(mostUsedTagsQuery.createQuery(certificates), Tag.class)
                .getResultList();
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        return Optional.empty();
    }
}
