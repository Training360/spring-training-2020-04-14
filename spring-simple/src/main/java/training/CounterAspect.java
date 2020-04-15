package training;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class CounterAspect {

    private AtomicInteger counter = new AtomicInteger();

    @Before("execution(* training.EmployeeDao.saveEmployee(..))")
    public void inc() {
        var value = counter.incrementAndGet();
        System.out.println("Aspect increment: " + value);
    }

}
