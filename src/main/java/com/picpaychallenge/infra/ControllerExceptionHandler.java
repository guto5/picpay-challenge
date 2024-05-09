package com.picpaychallenge.infra;

import com.picpaychallenge.dtos.ExceptionDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity threatDuplicatedEntry(DataIntegrityViolationException exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO("User already registered!", "400");
        return ResponseEntity.badRequest().body(exceptionDTO);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity threatNotFound(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity threatGeneralException(Exception exception) {
        ExceptionDTO exceptionDTO = new ExceptionDTO(exception.getMessage(), "500");
        return ResponseEntity.internalServerError().body(exceptionDTO);
    }

}
