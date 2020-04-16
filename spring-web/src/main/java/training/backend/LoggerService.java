package training.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class LoggerService implements ApplicationListener<EmployeeHasCreatedEvent> {

    @Override
    public void onApplicationEvent(EmployeeHasCreatedEvent employeeHasCreatedEvent) {
        log.info("Event has arrived: " + LocalDateTime.now() + " " + employeeHasCreatedEvent.getName());
    }
}
