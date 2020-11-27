package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import java.util.List;

public interface TagRepository extends BaseRepository<Tag> {
    /**
     * This method is used to create tags in database.
     *
     * @param tags the List of tags to create.
     * @return List of created tags.
     */
    List<Tag> createAll(List<Tag> tags);
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
    /**
     * This method is used to find tags in database
     * by their names.
     *
     * @param tags the List of tags with their names.
     * @return List of tags or empty List if
     *         no tags were found.
     */
    List<Tag> getTagsByName(List<Tag> tags);
}
