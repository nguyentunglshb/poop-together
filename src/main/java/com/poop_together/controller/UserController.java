package com.poop_together.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.request.CreateUserDto;
import com.poop_together.dto.response.UserResponse;
import com.poop_together.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController 
@RequestMapping ("/users")
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;
    
    @PostMapping()
     ApiResponse<UserResponse> createUser(@RequestBody CreateUserDto req) {
        return ApiResponse.<UserResponse>builder()
        .result(userService.createUser(req))
        .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable String userId) {
        return ApiResponse.<UserResponse>builder().result(userService.getUser(userId)).build();
    }

    @GetMapping()
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder().result(userService.getUsers()).build();
    }
    
    
}
