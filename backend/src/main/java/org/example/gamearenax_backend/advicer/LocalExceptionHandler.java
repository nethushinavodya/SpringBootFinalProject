package org.example.gamearenax_backend.advicer;

import org.example.gamearenax_backend.dto.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class LocalExceptionHandler {
    public ResponseEntity<ResponseDTO> handleException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ResponseDTO responceDto = new ResponseDTO(401, e.getMessage(), errors);
        return new ResponseEntity<>(responceDto, HttpStatus.BAD_REQUEST);
    }
}