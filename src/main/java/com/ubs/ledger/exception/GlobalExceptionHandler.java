package com.ubs.ledger.exception;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http
    .ResponseEntity;
import org.springframework.web.bind
    .annotation.ExceptionHandler;
import org.springframework.web.bind
    .annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOG =
        LoggerFactory.getLogger(
            GlobalExceptionHandler.class
        );

    @ExceptionHandler(
        ResourceNotFoundException.class
    )
    public ResponseEntity<Map<String, Object>>
        handleNotFound(
            ResourceNotFoundException ex
        ) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(Map.of(
                "error", ex.getMessage()
            ));
    }

    @ExceptionHandler(
        LedgerException.class
    )
    public ResponseEntity<Map<String, Object>>
        handleLedger(LedgerException ex) {
        LOG.error(
            "Ledger error: {}",
            ex.getMessage(), ex
        );
        return ResponseEntity
            .status(
                HttpStatus
                    .INTERNAL_SERVER_ERROR
            )
            .body(Map.of(
                "error", ex.getMessage()
            ));
    }

    @ExceptionHandler(
        IllegalArgumentException.class
    )
    public ResponseEntity<Map<String, Object>>
        handleBadRequest(
            IllegalArgumentException ex
        ) {
        return ResponseEntity
            .badRequest()
            .body(Map.of(
                "error", ex.getMessage()
            ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>>
        handleGeneral(Exception ex) {
        LOG.error(
            "Unexpected error: {}",
            ex.getMessage(), ex
        );
        return ResponseEntity
            .status(
                HttpStatus
                    .INTERNAL_SERVER_ERROR
            )
            .body(Map.of(
                "error",
                "something went wrong"
            ));
    }
}
