package com.epam.esm.service;

import com.epam.esm.converter.UserConverter;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EntityAlreadyExistsException;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.exception.EntityNotFoundException;
import com.epam.esm.model.Pagination;
import com.epam.esm.dto.UserTagDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.util.CollectionUtils.isEmpty;
import java.util.ArrayList;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static final long ERROR_CODE_NOT_FOUND = 40403;
    private static final long ERROR_CODE_CONFLICT = 40903;
    private static final String NOT_FOUND_EXCEPTION_KEY = "exception.user.not_found";
    private static final String CONFLICT_EXCEPTION_KEY = "exception.user.conflict";
    private static final String ILLEGAL_SEARCH_TYPE_KEY = "exception.illegal_search_type";
    private static final String MOST_USED = "most_used";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserConverter userConverter;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private TagRepository tagRepository;

    @Override
    public UserDto findById(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException(NOT_FOUND_EXCEPTION_KEY, id, ERROR_CODE_NOT_FOUND));
        return userConverter.toDto(user);
    }

    @Override
    public List<UserDto> findAll(Pagination pagination) {
        return userConverter.toDtoList(
                userRepository.findAll(pagination)
        );
    }

    @Override
    public List<UserTagDto> findUserTags(String searchType, String category) {
        if(searchType != null && searchType.equals(MOST_USED)){
            return getMostUsedUserTags(category);
        }
        throw new IllegalArgumentException(ILLEGAL_SEARCH_TYPE_KEY);
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        if(userRepository.findByLogin(userDto.getLogin()).isPresent()) {
            throw new EntityAlreadyExistsException(CONFLICT_EXCEPTION_KEY, ERROR_CODE_CONFLICT);
        }
        User user = userRepository.create(userConverter.toEntity(userDto)).orElse(new User());

        return userConverter.toDto(user);
    }

    private List<UserTagDto> getMostUsedUserTags(String userCategory){
        List <UserTagDto> tags = new ArrayList<>();
        List<User> users = userRepository.findAll(userCategory);

        for (User user: users) {
            List<Certificate> userCertificates = certificateRepository.getUserCertificates(user.getId());
            if(isEmpty(userCertificates)) {
                continue;
            }

            List<Tag> userTags = tagRepository.getMostUsedTags(userCertificates);
            if(isEmpty(userTags)) {
                continue;
            }

            UserTagDto userTagDto = new UserTagDto(user.getId(), user.getName(),userTags);
            tags.add(userTagDto);
        }
        return tags;
    }
}
