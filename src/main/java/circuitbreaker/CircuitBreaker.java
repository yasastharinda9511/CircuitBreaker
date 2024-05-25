package circuitbreaker;

import java.util.concurrent.Callable;

public class CircuitBreaker {

    public CircuitBreaker(int maxRetryCount, long retryTimeOut){
        this.retryTimeOut = retryTimeOut;
        this.maxRetryCount = maxRetryCount;

    }
    public<T> void execute(Callable<T> executeFunction) {

        updateState();

        if(state == CircuitBreakerState.OPEN) return ;

        try{
            executeFunction.call();
            handelSuccess();
        }catch (Exception e){
            handleFailure();
        }
    }


    public void updateState(){
        switch (state){
            case CLOSED, HALF_OPEN -> {}
            case OPEN -> {
                if(System.currentTimeMillis() - circuitOpenTime > retryTimeOut){
                    state = CircuitBreakerState.HALF_OPEN;
                }
            }
        }
    }

    public void handelSuccess(){
        switch (state){
            case CLOSED -> {}
            case OPEN, HALF_OPEN -> {
                state = CircuitBreakerState.CLOSED;
                currentRetryCount = 0;
            }
        }
    }
    public void handleFailure(){
        switch (state){
            case CLOSED -> {
                currentRetryCount++;
                if(currentRetryCount >= maxRetryCount){state = CircuitBreakerState.OPEN;}
            }
            case OPEN -> {
                circuitOpenTime = System.currentTimeMillis();
            }
            case HALF_OPEN -> {
                circuitOpenTime = System.currentTimeMillis();
                state = CircuitBreakerState.OPEN;
            }
        }
    }

    public CircuitBreakerState getState() {
        return state;
    }

    public int getMaxRetryCount(){
        return maxRetryCount;
    }

    public long getRetryTimeOut(){
        return  retryTimeOut;
    }
    private final int maxRetryCount;
    private int currentRetryCount = 0;
    private final long retryTimeOut;
    private long circuitOpenTime;
    CircuitBreakerState state = CircuitBreakerState.CLOSED;
}
