package org.example.orderservice.exception;

public class KafkaMessageNotSendException extends RuntimeException {

    public KafkaMessageNotSendException(String message) {
        super(message);
    }

    public KafkaMessageNotSendException(String message, Throwable cause) {
        super(message, cause);
    }
}
