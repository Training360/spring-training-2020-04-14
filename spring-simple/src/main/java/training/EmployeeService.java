package training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class EmployeeService {

    private final EmployeeDao employeeDao;

    private final ApplicationContext applicationContext;

    public EmployeeService(EmployeeDao employeeDao, ApplicationContext applicationContext) {
        this.employeeDao = employeeDao;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
        System.out.println("Service has been created");
    }

    public void saveEmployee(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty");
        }
        var upperCase = name.toUpperCase();
//        var upperCase = name;
//        employeeDao.getEmployees();
        employeeDao.saveEmployee(upperCase);
    }

    public Employee createDefaultEmployee() {
        return applicationContext.getBean(Employee.class);
    }

}
