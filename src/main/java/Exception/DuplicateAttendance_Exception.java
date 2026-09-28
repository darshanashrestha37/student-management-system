package Exception;

public class DuplicateAttendance_Exception extends Exception {

    public DuplicateAttendance_Exception(String message) {
        super(message);
    }

    public DuplicateAttendance_Exception(String message, Throwable cause) {
        super(message, cause);
    }
}