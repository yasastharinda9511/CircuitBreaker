import circuitbreaker.CircuitBreaker;
import circuitbreaker.CircuitBreakerState;
import org.junit.Test;

public class CircuitBreakerTest {

    @Test
    public void initTest(){
        CircuitBreaker cctBreaker = new CircuitBreaker(3, 1000);
        assert(3 == cctBreaker.getMaxRetryCount());
        assert(1000 == cctBreaker.getRetryTimeOut());
    }

    @Test
    public void testClosedState() throws InterruptedException {

        CircuitBreaker cctBreaker = new CircuitBreaker(3, 1000);
        cctBreaker.execute(()->{
            throw new Exception("just test the states !!!");
        });

        cctBreaker.execute(()->{
            throw new Exception("just test the states !!!");
        });

        cctBreaker.execute(()->{
            throw new Exception("just test the states !!!");
        });

        assert( CircuitBreakerState.OPEN == cctBreaker.getState());

        Thread.sleep(1000);

        cctBreaker.execute(()->{
            return "Yasas Tharinda";
        });

        assert( CircuitBreakerState.CLOSED == cctBreaker.getState());

        cctBreaker.execute(()->{
            throw new Exception("just test the states !!!");
        });

        assert( CircuitBreakerState.CLOSED == cctBreaker.getState());

        cctBreaker.execute(()->{
            throw new Exception("just test the states !!!");
        });

        assert( CircuitBreakerState.CLOSED == cctBreaker.getState());

        cctBreaker.execute(()->{
            throw new Exception("just test the states !!!");
        });

        assert( CircuitBreakerState.OPEN == cctBreaker.getState());
    }

}
