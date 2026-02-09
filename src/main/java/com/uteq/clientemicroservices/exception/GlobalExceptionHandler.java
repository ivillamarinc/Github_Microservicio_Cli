package com.uteq.clientemicroservices.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // TABLE-DRIVER: tabla que mapea código -> HTTP
    private static final Map<ApiErrorCode, HttpStatus> STATUS_TABLE = new EnumMap<>(ApiErrorCode.class);
    static {
        STATUS_TABLE.put(ApiErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST);      // 400
        STATUS_TABLE.put(ApiErrorCode.CLIENT_NOT_FOUND, HttpStatus.NOT_FOUND);        // 404
        STATUS_TABLE.put(ApiErrorCode.EMAIL_DUPLICATE, HttpStatus.CONFLICT);          // 409
        STATUS_TABLE.put(ApiErrorCode.DB_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);    // 500
        STATUS_TABLE.put(ApiErrorCode.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex) {
        HttpStatus status = STATUS_TABLE.getOrDefault(ex.getCode(), HttpStatus.INTERNAL_SERVER_ERROR);

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", ex.getCode().name());
        body.put("message", ex.getMessage());

        return ResponseEntity.status(status).body(body);
    }

    // Manejo típico de error de BD
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Object> handleDb(DataAccessException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", ApiErrorCode.DB_ERROR.name());
        body.put("message", "Error de base de datos");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    // Fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("code", ApiErrorCode.INTERNAL_ERROR.name());
        body.put("message", "Error interno");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}