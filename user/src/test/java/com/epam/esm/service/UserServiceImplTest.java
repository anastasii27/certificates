package com.epam.esm.service;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.Role;
import com.epam.esm.model.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringConfig.class)
@SpringBootTest
@EnableAutoConfiguration
class UserServiceImplTest {
    private static final String ILLEGAL_SEARCH_TYPE = "smth";
    private static final String CATEGORY = "user_orders_cost_max";
    @Mock
    private CertificateRepository certificateRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TagRepository tagRepository;
    @SpyBean
    private UserConverter userConverter;
    @InjectMocks
    private UserService userService = new UserServiceImpl();
    private User user;
    private UserDto userDto;
    private List<User> usersList;
    private List<UserDto> usersDtoList;
    private Pagination pagination;

    @BeforeEach
    void setUp() {
        pagination = new Pagination(100, 0);
        user = new User(1, "Nastya","login", "12345", Role.USER, null);
        userDto = new UserDto(1, "Nastya", "login", "12345", Role.USER);
        usersList = Collections.singletonList(user);
        usersDtoList = Collections.singletonList(userDto);
    }

    @Test
    void findById_whenUserExists_thenReturnUser() {
        when(userRepository.findById(1)).thenReturn(Optional.ofNullable(user));
        assertEquals(userService.findById(1), userDto);
    }

    @Test
    void findById_whenUserDoesNotExist_thenEntityNotFoundException() {
        when(userRepository.findById(9)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, ()-> userService.findById(9));
    }

    @Test
    void findAll_thenReturnAllUsersList() {
        when(userRepository.findAll(pagination)).thenReturn(usersList);
        assertIterableEquals(usersDtoList, userService.findAll(pagination));
    }

    @Test
    void findUserTags_whenSearchTypeIsNull_thenIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, ()-> userService.findUserTags(null, CATEGORY));
    }

    @Test
    void findUserTags_whenSearchTypeIsIllegal_thenIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, ()-> userService.findUserTags(ILLEGAL_SEARCH_TYPE, CATEGORY));
    }

    @Test
    void create_whenUserIsAlreadyExist_thenEntityAlreadyExistException() {
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.of(user));
        assertThrows(EntityAlreadyExistsException.class, ()-> userService.create(userDto));
    }

    @Test
    void create_whenUserDoesNotExist_thenReturnCreatedUser() {
        when(userRepository.findByLogin(userDto.getLogin())).thenReturn(Optional.empty());
        when(userRepository.create(user)).thenReturn(Optional.of(user));

        assertEquals(userDto, userService.create(userDto));
    }
}