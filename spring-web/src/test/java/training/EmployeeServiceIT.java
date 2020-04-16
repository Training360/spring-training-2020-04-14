package training;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import training.backend.Employee;
import training.backend.EmployeeService;
import training.backend.EmployeesConfig;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmployeesConfig.class)
@Sql(statements = "delete from employees")
public class EmployeeServiceIT {

    @Autowired
    private EmployeeService service;

    @BeforeEach
    void init() {
        service.emptyEmployees();
    }

    @Test
    void test_saveEmployee() {
        service.saveEmployee("John Doe");
        service.saveEmployee("Jack Doe");
        List<Employee> employees = service.listEmployees();
        assertEquals(List.of("JOHN DOE", "JACK DOE"), employees.stream().map(Employee::getName).collect(Collectors.toList()));
    }

    @Test
    void test_findEmployeeById() {
        var employee = service.saveEmployee("John Doe");
        var found = service.findEmployeeById(employee.getId());
        assertEquals("JOHN DOE", found.getName());
    }

    @Test
    void test_updateEmployee() {
        var employee = service.saveEmployee("John Doe");
        service.updateEmployee(employee.getId(), "Jack Doe");
        var found = service.findEmployeeById(employee.getId());
        assertEquals("JACK DOE", found.getName());
    }

    @Test
    void test_deleteEmployee() {
        var employee = service.saveEmployee("John Doe");
        service.deleteEmployee(employee.getId());
        List<Employee> employees = service.listEmployees();
        assertEquals(0, employees.size());
    }


}
