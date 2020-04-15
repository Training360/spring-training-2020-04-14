package training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmployeesConfig.class)
public class PrototypeEmployeeServiceIT {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testCreateDefaultEmployee() {
        var e1 = employeeService.createDefaultEmployee();
        var e2 = employeeService.createDefaultEmployee();
        System.out.println(e1);
        System.out.println(e2);
    }
}
