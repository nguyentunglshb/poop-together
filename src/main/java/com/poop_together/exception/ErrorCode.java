package com.poop_together.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter 
@FieldDefaults (level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(500, "Internal server error"),
    KEY_INVALID(400, "Invalid message key"),
    INVALID_REQUEST_BODY(400, "Request body is missing or invalid"),
    TOKEN_REQUIRED(400, "Token is required"),
    USER_EXISTED(409, "User already exists"),
    USERNAME_INVALID(400, "Username must be at least 3 characters"),
    PASSWORD_INVALID(400, "Password must be at least 8 characters"),
    USER_NOT_EXISTED(404, "User does not exist"),
    UNAUTHENTICATED(401, "Unauthenticated"),
    FRIEND_REQUEST_SELF(400, "Cannot send friend request to yourself"),
    FRIEND_REQUEST_ALREADY_SENT(400, "Friend request already sent or received")
    ;

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
