package projekt.wypozyczalnia.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import projekt.wypozyczalnia.exceptions.ConflictReservationException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(ConflictReservationException.class)
    public ResponseEntity<Object> handleConflictReservationException(ConflictReservationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
