package by.bsuir.poit.dao.exception;

/**
 * @author Paval Shlyk
 * @since 15/09/2023
 */
public class DataAccessException extends RuntimeException {
public DataAccessException(String message) {
      super(message);
}

public DataAccessException(Throwable t) {
      super(t);
}
}
