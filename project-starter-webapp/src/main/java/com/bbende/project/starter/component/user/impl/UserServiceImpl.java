package com.bbende.project.starter.component.user.impl;

import com.bbende.project.starter.component.user.UserDto;
import com.bbende.project.starter.component.user.UserNotFoundException;
import com.bbende.project.starter.component.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(final UserRepository userRepository, final UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto findByUsername(final String username) {
        final User user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
        return userMapper.toDto(user);
    }
}
