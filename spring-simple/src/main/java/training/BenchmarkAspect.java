package training;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BenchmarkAspect {

    @Around("execution(* training.EmployeeDao.saveEmployee(..))")
    public void benchmark(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        joinPoint.proceed();
        System.out.println("Benchmark (ms): " + (System.currentTimeMillis() - start));
    }
}
