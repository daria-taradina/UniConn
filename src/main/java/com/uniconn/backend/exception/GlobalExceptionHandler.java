package com.uniconn.backend.exception;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// 400 - @Valid annotation failures; collects all field errors into one map
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
          .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }
        
    // 404 - thrown manually when requested resource doesn't exist
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity.status(404)
            .body(Map.of("error", ex.getMessage()));
    }
    
    // 409 - thrown manually when resource already exists (e.g., duplicate community name)
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleAlreadyExists(
            ResourceAlreadyExistsException ex) {
        return ResponseEntity.status(409)
            .body(Map.of("error", ex.getMessage()));
    }

    // 403 - thrown manually when authenticated user lacks permission
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorized(
            UnauthorizedException ex) {
        return ResponseEntity.status(403)
            .body(Map.of("error", ex.getMessage()));
    }
    
    // 400 - Spring throws it on DB constraint violations (e.g., unique, not-null)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDbConstraint(
            DataIntegrityViolationException ex) {
        return ResponseEntity.badRequest()
            .body(Map.of("error", "A database constraint was violated"));
    }
    
    // 400 - thrown manually for bad input that passes @Valid but fails at service layer
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<Map<String, String>> handleInvalidInput(
            InvalidInputException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    // 500 - catch-all for anything unhandled 
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        return ResponseEntity.internalServerError()
            .body(Map.of("error", "Something went wrong"));
    }
}
