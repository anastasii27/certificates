package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCertificateDto {
    @NotNull
    @FutureOrPresent
    private ZonedDateTime purchaseDate;
    @NotNull
    @NotEmpty
    private List<CertificateDto> orderedCertificates;
}
