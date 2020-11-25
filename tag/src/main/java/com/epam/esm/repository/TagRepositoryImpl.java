package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("tagRepository")
public class TagRepositoryImpl implements TagRepository {
    private static final String TAGS_LIST  = "SELECT t FROM Tag t";
    private static final String TAGS_BY_NAME  = "SELECT t FROM Tag t WHERE t.name=?1";
    private static final String CERTIFICATE_TAGS = "SELECT `id`, `name` FROM `gift-certificates`.`tag_m2m_gift_certificate`\n" +
            "JOIN tag ON `tag_m2m_gift_certificate`.`tagId` = tag.id\n" +
            "WHERE `giftCertificateId`=?;";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> createAll(List<Tag> tags) {
        for (Tag tag: tags) {
            entityManager.persist(tag);
        }
        return tags;
    }

    @Override
    public List<Tag> getCertificateTags(long certificateId) {
        return entityManager.createNativeQuery(CERTIFICATE_TAGS, Tag.class)
                .setParameter(1, certificateId)
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
    public List<Tag> getTagsByName(List<Tag> tags) {
        List<Tag> tagsListToReturn = new ArrayList<>();

        for (Tag tag:tags){
            Tag tagToReturn = entityManager.createQuery(TAGS_BY_NAME, Tag.class)
                    .setParameter(1, tag.getName())
                    .getResultList()
                    .stream().findFirst().orElse(null);
            tagsListToReturn.add(tagToReturn);
        }
        return tagsListToReturn;
    }

    @Override
    public Optional<Tag> update(Tag tag) {
        return Optional.empty();
    }
}
