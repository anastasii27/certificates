package com.epam.esm.converter;

import com.epam.esm.dto.UserDto;
import com.epam.esm.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserConverter {
    @Autowired
    private ModelMapper modelMapper;

    public User toEntity(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

    public UserDto toDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    public List<UserDto> toDtoList(List<User> entityList){
        return entityList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
