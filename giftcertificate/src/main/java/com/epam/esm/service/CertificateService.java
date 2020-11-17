package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import java.util.List;
import java.util.Map;

public interface CertificateService {
    /**
     * This method is used to return all certificates.
     *
     * @return the List of all certificates or empty List
     *          if no certificates were found.
     */
    List<CertificateDto> findAll(Pagination pagination);
    /**
     * This method is used to return certificate by id.
     *
     * @param id the id of certificate to be returned.
     * @return searched certificate.
     * @throws EntityNotFoundException if no certificate was found.
     */
    CertificateDto findById(long id);
    /**
     * This method is used to delete the certificate by id.
     *
     * @param id the id of certificate to be deleted.
     * @throws EntityNotFoundException if certificate does not exist.
     */
    void delete(long id);
    /**
     * This method is used to create the certificate and return created one.
     *
     * @param certificate the certificate to be created.
     * @return if certificate was created a created Certificate
     *          is returned otherwise an empty Certificate is returned.
     * @throws EntityAlreadyExistsException if certificate is already exist.
     */
    CertificateDto create(CertificateDto certificate);
    /**
     * This method is used to update the certificate and return
     * updated one.
     *
     * @param certificate the certificate to be updated.
     * @return if certificate was updated an updated Certificate
     *          is returned otherwise an empty Certificate is returned.
     * @throws EntityNotFoundException if certificate does not exist.
     * @throws EntityAlreadyExistsException if certificate is already exist.
     */
    CertificateDto update(CertificateDto certificate, long id);
    /**
     * This method is used to filter certificates.
     *
     * @param filterParams the Map of params by which certificates
     *                     have to be filtered. If Map contains illegal filter params
     *                     they will be ignored.
     * @return List of filtered certificates or empty List if no
     *          certificates were found.
     */
    List<CertificateDto> getFilteredCertificates(Map<String, String> filterParams, Pagination pagination);
}
