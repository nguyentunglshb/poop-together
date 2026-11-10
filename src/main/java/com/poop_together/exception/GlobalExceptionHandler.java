package com.poop_together.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.poop_together.dto.ApiResponse;

@ControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler (value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception){
        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(apiResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    ResponseEntity<ApiResponse> handlingUnreadableBody() {
        return handlingAppException(new AppException(ErrorCode.INVALID_REQUEST_BODY));
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(apiResponse);
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Map<String, String>>> handlingValidationException(MethodArgumentNotValidException exception) {
        ErrorCode errorCode = ErrorCode.INPUT_NOT_VALID;
        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>();
        
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        apiResponse.setResult(errors);
        return ResponseEntity.status(errorCode.getCode()).body(apiResponse);
    }
}
