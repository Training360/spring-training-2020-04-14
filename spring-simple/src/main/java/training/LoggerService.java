package training;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoggerService implements ApplicationListener<EmployeeHasCreatedEvent> {

    @Override
    public void onApplicationEvent(EmployeeHasCreatedEvent employeeHasCreatedEvent) {
        System.out.println("Event has arrived: " + LocalDateTime.now() + " " + employeeHasCreatedEvent.getName());
    }
}
