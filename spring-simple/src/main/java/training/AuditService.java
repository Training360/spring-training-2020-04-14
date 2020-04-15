package training;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AuditService implements ApplicationListener<EmployeeHasCreatedEvent> {

    private AtomicInteger counter = new AtomicInteger();

    @Override
    public void onApplicationEvent(EmployeeHasCreatedEvent employeeHasCreatedEvent) {
        var value = counter.incrementAndGet();
        System.out.println("Incremented: " + value);
    }
}
