package com.epam.esm.dto;

import com.epam.esm.model.Role;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends RepresentationModel<UserDto> {
    private long id;
    @NotNull
    private String name;
    @NotNull
    @Length(min = 4, max = 15)
    private String login;
    @NotNull
    @Length(min = 4, max = 15)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @NotNull
    private Role role;
}
