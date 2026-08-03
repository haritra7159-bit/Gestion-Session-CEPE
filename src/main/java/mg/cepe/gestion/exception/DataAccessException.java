package mg.cepe.gestion.exception;

/** Erreur technique d'accès à la base de données. */
public class DataAccessException extends RuntimeException {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
