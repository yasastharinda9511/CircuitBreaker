package circuitbreaker.exceptions;

import circuitbreaker.CircuitBreakerState;

public class CircuitBreakerOpenException extends Exception{
    public CircuitBreakerOpenException(String message){
        super(message);
    }
}
