package org.example.orderhub_project.order;

import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<Error> handleException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField()+":" + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(new Error("validation failed", errors));

    }
}
