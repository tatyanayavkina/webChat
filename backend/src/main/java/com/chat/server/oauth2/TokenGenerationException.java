package com.chat.server.oauth2;

/**
 * Created on 10.11.2015.
 */
public class TokenGenerationException extends Exception {

    public TokenGenerationException() {
    }

    public TokenGenerationException(String message) {
        super(message);
    }

    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenGenerationException(Throwable cause) {
        super(cause);
    }
}