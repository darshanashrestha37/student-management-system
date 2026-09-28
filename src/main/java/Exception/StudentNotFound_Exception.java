package Exception;

public class StudentNotFound_Exception extends Exception {

    public StudentNotFound_Exception(String message) {
        super(message);
    }

    public StudentNotFound_Exception(String message, Throwable cause) {
        super(message, cause);
    }
}