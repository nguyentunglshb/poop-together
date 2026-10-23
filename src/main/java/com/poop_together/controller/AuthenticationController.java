package com.poop_together.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.poop_together.dto.ApiResponse;
import com.poop_together.dto.request.AuthenticationRequest;
import com.poop_together.dto.request.IntrospectRequest;
import com.poop_together.dto.response.AuthenticationResponse;
import com.poop_together.dto.response.IntrospectResponse;
import com.poop_together.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping ("/auth")
@RequiredArgsConstructor 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;

    @PostMapping ("/token")
    ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest req) {
        var result = authenticationService.authenticated(req);
        return ApiResponse.<AuthenticationResponse>builder().result(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest req) throws JOSEException, ParseException {
        var result = authenticationService.introspect(req);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }
    
}
