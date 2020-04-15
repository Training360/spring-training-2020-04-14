package training;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class EmployeeService {

    private final EmployeeDao employeeDao;

    private final ApplicationContext applicationContext;

    private final ApplicationEventPublisher publisher;

//    private final Environment env;

    private final boolean uppercaseEnabled;

    public EmployeeService(EmployeeDao employeeDao, ApplicationContext applicationContext, ApplicationEventPublisher publisher, @Value("${uppercase.enabled}") boolean uppercaseEnabled) {
        this.employeeDao = employeeDao;
        this.applicationContext = applicationContext;
        this.publisher = publisher;
        this.uppercaseEnabled = uppercaseEnabled;
    }

    @PostConstruct
    public void init() {
        log.info("Service has been created");
    }

    public void saveEmployee(String name) {
        log.info("Save employee");
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty");
        }

        String changedName;
//        if (Boolean.parseBoolean(env.getProperty("uppercase.enabled"))) {
        if (uppercaseEnabled) {
            changedName = name.toUpperCase();
        }
        else {
            changedName = name;
        }

//        var upperCase = name;
//        employeeDao.getEmployees();
        employeeDao.saveEmployee(changedName);

        publisher.publishEvent(new EmployeeHasCreatedEvent(this, name));
    }

    public Employee createDefaultEmployee() {
        return applicationContext.getBean(Employee.class);
    }

}
