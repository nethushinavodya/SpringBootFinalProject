package org.example.gamearenax_backend.advicer;

import org.example.gamearenax_backend.dto.ResponseDTO;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppWideExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseDTO ExceptionHandler(Exception e){
        e.printStackTrace();
        return new ResponseDTO(500, "Internal Server Error", e.getMessage());
    }
}