package br.com.ks.teste.uds.exception;

/**
 *
 * @author Jonny
 */
public class CustomException extends Exception{

    private String message;
    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
