package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import java.util.List;

public interface TagRepository extends BaseRepository<Tag> {
    /**
     * This method is used to create tags in database.
     *
     * @param tags the List of tags to create.
     */
    void createAll(List<Tag> tags);
    /**
     * This method is used to add tag to existing certificate
     * in database.
     *
     * @param tags          the List of tags for adding.
     * @param certificateId the id of certificate.
     */
    void addTagsToCertificate(List<Tag> tags, long certificateId);
    /**
     * This method is used to delete tag from database from existing certificate.
     *
     * @param tags          the List of tags for deleting.
     * @param certificateId the id of certificate.
     */
    void deleteCertificateTags(List<Tag> tags, long certificateId);
    /**
     * This method is used to return certificate tags from database by
     * certificate id.
     *
     * @param certificateId the id of certificate.
     * @return List of certificate tags or empty List if
     *         no tags were found.
     */
    List<Tag> getCertificateTags(long certificateId);
    /**
     * This method is used to return from database the most widely
     * used certificates tags.
     *
     * @param certificates certificates among which to find tags.
     * @return List of tags or empty List if
     *         no tags were found.
     */
    List<Tag> getMostUsedTags(List<Certificate> certificates);
}
