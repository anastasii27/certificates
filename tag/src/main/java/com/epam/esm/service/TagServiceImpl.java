package com.epam.esm.service;

import com.epam.esm.converter.TagConverter;
import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service("tagService")
public class TagServiceImpl implements TagService {
    private static final long ERROR_CODE_NOT_FOUND = 40401;
    private static final long ERROR_CODE_CONFLICT = 40901;
    private static final String NOT_FOUND_EXCEPTION_KEY = "exception.tag.not_found";
    private static final String CONFLICT_EXCEPTION_KEY = "exception.tag.conflict";
    private static final int DEFAULT_LIMIT = 1000;
    private static final int DEFAULT_OFFSET = 0;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TagConverter tagConverter;

    @Override
    @Transactional
    public TagDto create(TagDto tagDto) {
        Tag tag = tagConverter.toEntity(tagDto);
        if(findNonExistentTags(Collections.singletonList(tag)).size() == 0){
            throw new EntityAlreadyExistsException(CONFLICT_EXCEPTION_KEY, ERROR_CODE_CONFLICT);
        }

        Optional<Tag> createdTag = tagRepository.create(tag);
        if(createdTag.isPresent()){
            return tagConverter.toDto(createdTag.get());
        }else {
            return new TagDto();
        }
    }

    @Override
    public List<TagDto> findAll(Pagination pagination) {
        return tagConverter.toDtoList(
                tagRepository.findAll(pagination)
        );
    }

    @Override
    public TagDto findById(long id) {
       Tag tag = tagRepository.findById(id)
               .orElseThrow(()-> new EntityNotFoundException(NOT_FOUND_EXCEPTION_KEY, id, ERROR_CODE_NOT_FOUND));
       return tagConverter.toDto(tag);
    }

    @Override
    @Transactional
    public void delete(long id) {
        if(!tagRepository.findById(id).isPresent()){
            throw new EntityNotFoundException(NOT_FOUND_EXCEPTION_KEY, id, ERROR_CODE_NOT_FOUND);
        }
        tagRepository.delete(id);
    }

    @Override
    @Transactional
    public void addTagsToCertificate(List<TagDto> tags, long certificateId) {
        if(!isEmpty(tags)) {
            List<Tag> entityList = tagConverter.toEntityList(tags);
            createNonExistentTags(tagConverter.toEntityList(tags));

            if (!isEmpty(tags)) {
                tagRepository.addTagsToCertificate(entityList, certificateId);
            }
        }
    }

    @Override
    @Transactional
    public void updateCertificateTags(List<TagDto> updatedTags, long certificateId) {
        if(!isEmpty(updatedTags)) {
            List<Tag> entityList = tagConverter.toEntityList(updatedTags);
            createNonExistentTags(entityList);

            List<Tag> oldCertificateTags = tagRepository.getCertificateTags(certificateId);
            tagRepository.deleteCertificateTags(listsDifference(oldCertificateTags, entityList), certificateId);
            tagRepository.addTagsToCertificate(listsDifference(entityList, oldCertificateTags), certificateId);
        }
    }

    @Override
    public List<Tag> getCertificateTags(long certificateId) {
        return tagRepository.getCertificateTags(certificateId);
    }

    private void createNonExistentTags(List<Tag> tags){
        List<Tag> nonExistentTags = findNonExistentTags(tags);

        if(!isEmpty(nonExistentTags)) {
            tagRepository.createAll(nonExistentTags);
        }
    }

    private List<Tag> findNonExistentTags(List<Tag> tags) {
        List<Tag> existingTags = tagRepository.findAll(new Pagination(DEFAULT_LIMIT, DEFAULT_OFFSET));
        return listsDifference(tags, existingTags);
    }

    private List<Tag> listsDifference(List<Tag> firstList, List<Tag> secondList){
        if(firstList == null || secondList == null){
            return Collections.emptyList();
        }
        return firstList.stream()
                .distinct()
                .filter(tag ->secondList.stream()
                        .noneMatch(tag1 -> tag1.getName().equals(tag.getName())))
                .collect(Collectors.toList());
    }
}

