package com.epam.esm.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(builder = CertificateDto.CertificateDtoBuilder.class)
@Builder(builderClassName = "CertificateDtoBuilder")
public class CertificateDto extends RepresentationModel<CertificateDto> {
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @PositiveOrZero
    private BigDecimal price;
    @FutureOrPresent
    private ZonedDateTime createDate;
    @FutureOrPresent
    private ZonedDateTime lastUpdateDate;
    @Positive
    private long duration;
    private List<TagDto> tags;

    @JsonPOJOBuilder(withPrefix = "")
    public static class CertificateDtoBuilder{

    }
}
