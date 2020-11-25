package com.epam.esm.repository;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Pagination;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CertificateRepository extends BaseRepository<Certificate> {
    /**
     * This method is used to return filtered certificates from database.
     *
     * @param filterParams the Map of params by which certificates
     *                     have to be filtered.
     * @return List of filtered certificates.
     */
    List getFilteredCertificates(Map<String, String> filterParams, Pagination pagination);
    /**
     * This method is used to return certificate from database
     * by its name and description.
     *
     * @param name the name of certificate to return.
     * @param description the description of certificate to return.
     * @return an Optional with the value of Certificate or empty Optional
     *          if Certificate does not exist.
     */
    Optional<Certificate> getCertificate(String name, String description);
    /**
     * This method is used to return all user certificates from database.
     *
     * @param userId the id of a user whose certificates to return.
     * @return List of certificates or empty List if certificates do not exist.
     */
    List<Certificate> getUserCertificates(long userId);
}
