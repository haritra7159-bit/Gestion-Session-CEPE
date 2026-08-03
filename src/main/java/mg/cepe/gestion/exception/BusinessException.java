package mg.cepe.gestion.exception;

/** Violation d'une règle métier (validation, unicité, etc.). */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
