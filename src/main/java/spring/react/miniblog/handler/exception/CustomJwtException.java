package spring.react.miniblog.handler.exception;

public class CustomJwtException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public CustomJwtException(String message) {
        super(message);
    }
}
