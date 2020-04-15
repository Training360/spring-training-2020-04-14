package training;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {

    @Around("execution(* training.EmployeeDao.saveEmployee(..))")
    public void checkName(ProceedingJoinPoint joinPoint) throws Throwable {
        var name = (String) joinPoint.getArgs()[0];

        if (!name.toLowerCase().contains("admin")) {
            joinPoint.proceed(new Object[]{"XXXXXXXXX"});
        }
    }
}
