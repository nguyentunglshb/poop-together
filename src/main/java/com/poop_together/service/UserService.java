package com.poop_together.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.poop_together.dto.request.CreateUserDto;
import com.poop_together.dto.response.UserResponse;
import com.poop_together.entity.User;
import com.poop_together.exception.AppException;
import com.poop_together.exception.ErrorCode;
import com.poop_together.mapper.UserMapper;
import com.poop_together.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service 
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserResponse createUser(CreateUserDto req) {
        if(userRepository.existsByUsername(req.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(req);
        user.setPassword_hash(req.getPassword());

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public UserResponse getUser(String userId) {
        return userMapper.toUserResponse(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
    }

    public List<UserResponse> getUsers() {
        return userMapper.toUserResponses(userRepository.findAll());
    }
}
