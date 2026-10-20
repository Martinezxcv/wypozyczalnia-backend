package projekt.wypozyczalnia.exceptions;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ConflictReservationException extends RuntimeException {

    public ConflictReservationException(String message) {
        super(message);
    }

}

