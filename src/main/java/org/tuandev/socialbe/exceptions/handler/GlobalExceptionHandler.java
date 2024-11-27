package org.tuandev.socialbe.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.tuandev.socialbe.dto.response.Response;
import org.tuandev.socialbe.exceptions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Response> handleUserNotFoundException(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Response.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.NOT_FOUND.value())
                        .timestamp(LocalDateTime.now())
                .build());
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<Response> handleUserAlreadyExistsException(AlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                Response.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.CONFLICT.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(NotAuthenticatedException.class)
    public ResponseEntity<Response> handleUserNotAuthenticatedException(NotAuthenticatedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                Response.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.UNAUTHORIZED.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(InsufficientQuantityException.class)
    public ResponseEntity<Response> handleInsufficientQuantityException(InsufficientQuantityException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Response.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                Response.builder()
                        .message("Fill is required")
                        .code(HttpStatus.BAD_REQUEST.value())
                        .error(errors)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
