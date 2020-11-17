package com.epam.esm.service;

import com.epam.esm.dto.CertificateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UpdatedCertificate {
    private final List<String> illegalMapKeys = Arrays.asList("links");
    private final List<Object> illegalMapValues = Arrays.asList(null, 0L);
    @Autowired
    private ObjectMapper objectMapper;

    public CertificateDto getUpdatedCertificate(CertificateDto newCertificate, CertificateDto oldCertificate){
        Map<String, Object> newCertificateMap = certificateToMap(newCertificate);
        Map<String, Object> oldCertificateMap = certificateToMap(oldCertificate);

        oldCertificateMap.putAll(newCertificateMap);

        return objectMapper.convertValue(oldCertificateMap, CertificateDto.class);
    }

    private Map<String, Object> certificateToMap(CertificateDto certificate){
        Map<String, Object> giftCertificateMap = objectMapper.convertValue(certificate, HashMap.class);

        giftCertificateMap.values().removeAll(illegalMapValues);
        giftCertificateMap.keySet().removeAll(illegalMapKeys);

        return giftCertificateMap;
    }
}
