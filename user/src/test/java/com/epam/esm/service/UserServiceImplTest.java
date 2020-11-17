package com.epam.esm.service;

import com.epam.esm.config.SpringConfig;
import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import com.epam.esm.model.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.mockito.PowerMockito.when;

@ExtendWith(MockitoExtension.class)
@SpringJUnitConfig(SpringConfig.class)
@SpringBootTest
@EnableConfigurationProperties
@ActiveProfiles("test")
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
        user = new User(1, "Nastya",null);
        userDto = new UserDto(1, "Nastya");
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
    void findUserTags_whenSearchTypeIsNull_thenReturnEmptyList() {
        assertIterableEquals(Collections.emptyList(), userService.findUserTags(null, CATEGORY));
    }

    @Test
    void findUserTags_whenSearchTypeIsIllegal_thenReturnEmptyList() {
        assertIterableEquals(Collections.emptyList(), userService.findUserTags(ILLEGAL_SEARCH_TYPE, CATEGORY));
    }
}