package com.epam.esm.service;

import  com.epam.esm.converter.CertificateConverter;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import com.epam.esm.service.utils.UpdatedCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static com.epam.esm.service.utils.FilterParameterUtils.*;

@Service("certificateService")
public class CertificateServiceImpl implements CertificateService {
    private static final String NOT_FOUND_EXCEPTION_KEY = "exception.certificate.not_found";
    private static final String CONFLICT_EXCEPTION_KEY = "exception.certificate.conflict";
    private static final long ERROR_CODE_NOT_FOUND = 40402;
    private static final long ERROR_CODE_CONFLICT = 40902;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private TagService tagService;
    @Autowired
    private CertificateConverter certificateConverter;
    @Autowired
    private UpdatedCertificate updatedCertificate;

    @Override
    public List<CertificateDto> findAll(Pagination pagination) {
        return certificateConverter.toDtoList(
                certificateRepository.findAll(pagination)
        );
    }

    @Override
    public CertificateDto findById(long id) {
        Certificate certificate = certificateRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(NOT_FOUND_EXCEPTION_KEY, id, ERROR_CODE_NOT_FOUND));
        return certificateConverter.toDto(certificate);
    }

    @Override
    @Transactional
    public void delete(long id) {
        if(!certificateRepository.findById(id).isPresent()) {
            throw new EntityNotFoundException(NOT_FOUND_EXCEPTION_KEY, id, ERROR_CODE_NOT_FOUND);
        }
        certificateRepository.delete(id);
    }

    @Override
    @Transactional
    public CertificateDto create(CertificateDto certificateDto) {
        certificateRepository.getCertificate(certificateDto.getName(), certificateDto.getDescription())
                .ifPresent(c -> {
                        throw new EntityAlreadyExistsException(CONFLICT_EXCEPTION_KEY, ERROR_CODE_CONFLICT);
                    }
                );
        Certificate certificate = certificateConverter.toEntity(certificateDto);

        Optional<Certificate> optionalCertificate = certificateRepository.create(certificate);
        if(optionalCertificate.isPresent()){
            Certificate createdCertificate = optionalCertificate.get();
            addTagsToCertificate(createdCertificate, certificateDto.getTags());

            return certificateConverter.toDto(createdCertificate);
        }else {
            return new CertificateDto();
        }
    }

    private void addTagsToCertificate(Certificate certificate, List<TagDto> tags){
        long id = certificate.getId();

        tagService.addTagsToCertificate(tags, id);
        Set<Tag> tagsSet = new HashSet<>(tagService.getCertificateTags(id));
        certificate.setTags(tagsSet);
    }

    @Override
    @Transactional
    public CertificateDto update(CertificateDto certificateDto, long id) {
        certificateRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(NOT_FOUND_EXCEPTION_KEY, id, ERROR_CODE_NOT_FOUND));
        certificateRepository.getCertificate(certificateDto.getName(), certificateDto.getDescription())
                .ifPresent(c -> {
                            throw new EntityAlreadyExistsException(CONFLICT_EXCEPTION_KEY, ERROR_CODE_CONFLICT);
                        }
                );
        CertificateDto modifiedCertificate = updatedCertificate.getUpdatedCertificate(certificateDto, findById(id));

        Optional<Certificate> optionalUpdCertificate =
                certificateRepository.update(certificateConverter.toEntity(modifiedCertificate));
        if(optionalUpdCertificate.isPresent()){
            Certificate updatedCertificate = optionalUpdCertificate.get();
            updateCertificateTags(updatedCertificate, certificateDto.getTags());

            return certificateConverter.toDto(updatedCertificate);
        }else {
            return new CertificateDto();
        }
    }

    private void updateCertificateTags(Certificate certificate, List<TagDto> tags){
        long id = certificate.getId();

        tagService.updateCertificateTags(tags,id);
        Set<Tag> tagsSet = new HashSet<>(tagService.getCertificateTags(id));
        certificate.setTags(tagsSet);
    }

    @Override
    public List<CertificateDto> getFilteredCertificates(Map<String, String> filterParams, Pagination pagination) {
        if(filterParams == null){
            return Collections.emptyList();
        }

        removeIllegalSearchParams(filterParams);
        removeIllegalSortDirectionParams(filterParams);
        modifyParamsValuesForDatabase(filterParams);

        return certificateConverter.toDtoList(
                certificateRepository.getFilteredCertificates(filterParams, pagination)
        );
    }
}
