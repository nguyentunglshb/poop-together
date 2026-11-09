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
    FRIEND_REQUEST_NOT_FOUND(404, "Friend request not found"),
    UNAUTHENTICATED(401, "Unauthenticated"),
    FRIEND_REQUEST_SELF(400, "Cannot send friend request to yourself"),
    FRIEND_REQUEST_ALREADY_SENT(400, "Friend request already sent or received"),
    FRIEND_REQUEST_NOT_AUTHORIZED(403, "Not authorized to change the status of this friend request"),
    FRIEND_REQUEST_ALREADY_RESPONDED(400, "Friend request has already been responded to"),
    SESSION_ALREADY_ACTIVE(400, "Session already active")
    ;

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
