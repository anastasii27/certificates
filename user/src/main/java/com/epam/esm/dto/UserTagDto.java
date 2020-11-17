package com.epam.esm.dto;

import com.epam.esm.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTagDto extends RepresentationModel<UserTagDto> {
    private long id;
    private String name;
    private List<Tag> tags;
}
