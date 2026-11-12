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

    // Friend
    FRIEND_REQUEST_NOT_FOUND(404, "Friend request not found"),
    FRIEND_REQUEST_SELF(400, "Cannot send friend request to yourself"),
    FRIEND_REQUEST_ALREADY_SENT(400, "Friend request already sent or received"),
    FRIEND_REQUEST_NOT_AUTHORIZED(403, "Not authorized to change the status of this friend request"),
    FRIEND_REQUEST_ALREADY_RESPONDED(400, "Friend request has already been responded to"),

    // Session
    SESSION_ALREADY_ACTIVE(400, "Session already active"),
    SESSION_NOT_ACTIVE(400, "No active session found"),
    INPUT_NOT_VALID(400, "Input is not valid"),

    // Conversation
    CONVERSATION_NOT_FOUND(404, "Conversation not found"),
    CONVERSATION_MEMBER_NOT_FOUND(404, "Conversation member not found"),
    NOT_FRIENDS(403, "You are not friends with this user"),
    NOT_CONVERSATION_MEMBER(403, "You are not a member of this conversation"),
    MESSAGE_INVALID(400, "Message is invalid"),
    CONVERSATION_WITH_SELF(400, "Cannot create a conversation with yourself")
    ;

    int code;
    String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
