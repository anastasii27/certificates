package com.epam.esm.converter;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.model.Certificate;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CertificateConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Certificate toEntity(CertificateDto certificateDto) {
        Certificate certificate = modelMapper.map(certificateDto, Certificate.class);

        certificate.setCreateDate(getLocalDateTime(certificateDto.getCreateDate()));
        certificate.setCreateDateTimezone(getZoneId(certificateDto.getCreateDate()));

        certificate.setLastUpdateDate(getLocalDateTime(certificateDto.getLastUpdateDate()));
        certificate.setLastUpdateDateTimezone(getZoneId(certificateDto.getLastUpdateDate()));

        return certificate;
    }

    public CertificateDto toDto(Certificate certificate) {
        CertificateDto certificateDto = modelMapper.map(certificate, CertificateDto.class);

        ZonedDateTime createDate = ZonedDateTime.of(
                certificate.getCreateDate(),
                certificate.getCreateDateTimezone()
        );
        ZonedDateTime lastUpdateDate = ZonedDateTime.of(
                certificate.getLastUpdateDate(),
                certificate.getLastUpdateDateTimezone()
        );
        certificateDto.setCreateDate(createDate);
        certificateDto.setLastUpdateDate(lastUpdateDate);

        return certificateDto;
    }

    public List<Certificate> toEntityList(List<CertificateDto> dtoList){
        return  dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public List<CertificateDto> toDtoList(List<Certificate> entityList){
        return  entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private ZoneId getZoneId(ZonedDateTime dateTime){
        return dateTime.getZone();
    }

    private LocalDateTime getLocalDateTime(ZonedDateTime dateTime){
        return dateTime.toLocalDateTime();
    }
}
