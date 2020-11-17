package com.epam.esm.converter;

import com.epam.esm.dto.TagDto;
import com.epam.esm.model.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagConverter {
    @Autowired
    private ModelMapper modelMapper;

    public Tag toEntity(TagDto tagDto){
        return modelMapper.map(tagDto, Tag.class);
    }

    public TagDto toDto(Tag tag){
        return modelMapper.map(tag, TagDto.class);
    }

    public List<TagDto> toDtoList(List<Tag> entityList){
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<Tag> toEntityList(List<TagDto> dtoList){
        return dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
