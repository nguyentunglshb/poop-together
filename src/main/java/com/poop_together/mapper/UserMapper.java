package com.poop_together.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.poop_together.dto.request.CreateUserDto;
import com.poop_together.dto.response.UserResponse;
import com.poop_together.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(CreateUserDto req);

    UserResponse toUserResponse(User user);
    List<UserResponse> toUserResponses(List<User> users);
    void updateUser(CreateUserDto req, @MappingTarget User user);
}
