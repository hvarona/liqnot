package bo;

/**
 * Created by henry on 15/01/2017.
 */

public class InvalidValuesException extends Exception {

    public InvalidValuesException(String message) {
        super(message);
    }

    public InvalidValuesException(String message, Throwable cause) {
        super(message, cause);
    }
}
