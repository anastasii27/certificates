package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto extends RepresentationModel<TagDto>{
    private long id;
    @NotBlank
    @Pattern(regexp = "#[\\p{Ll}\\p{Lu}]+")
    private String name;
}
