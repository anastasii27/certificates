package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Tag;
import java.util.List;

public interface TagService {
    /**
     * This method is used to create a tag and return created one.
     *
     * @param tag the tag to be created.
     * @return created tag or empty tag if it was not created.
     * @throws EntityAlreadyExistsException if tag is already exist.
     */
    TagDto create(TagDto tag);
    /**
     * This method is used to return the List of tags.
     *
     * @return List of all tags or empty List if
     *         no tags were found.
     */
    List<TagDto> findAll(Pagination pagination);
    /**
     * This method is used to return tag by id.
     *
     * @param id the id of tag to be returned.
     * @return searched tag.
     * @throws EntityNotFoundException if tag does not exist.
     */
    TagDto findById(long id);
    /**
     * This method is used to delete tag by id.
     *
     * @param id the id of tag to be deleted.
     * @throws EntityNotFoundException if tag does not exist.
     */
    void delete(long id);
    /**
     * This method is used to add tag to existing certificate.
     *
     * @param tags          the List of tags for adding.
     * @param certificate   the certificate to update.
     * @throws EntityNotFoundException if tag does not exist.
     */
    void addTagsToCertificate(List<TagDto> tags, Certificate certificate);
    /**
     * This method is used to return certificate tags by
     * certificate id.
     *
     * @param certificateId the id of certificate.
     * @return List of certificate tags or empty List if
     *         no tags were found.
     */
    List<Tag> getCertificateTags(long certificateId);
}
