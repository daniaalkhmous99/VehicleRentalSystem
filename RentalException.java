// Our own exception, used when a business rule is broken
// (for example: renting a vehicle that is not available).
public class RentalException extends Exception {

    public RentalException(String message) {
        super(message);
    }
}
