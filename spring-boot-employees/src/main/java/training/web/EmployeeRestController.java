package training.web;

import org.springframework.web.bind.annotation.*;
import training.backend.Employee;
import training.backend.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeRestController {

    private EmployeeService employeeService;

    public EmployeeRestController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> listEmployees() {
        return employeeService.listEmployees();
    }

    @PostMapping
    public Employee saveEmployee(@RequestBody CreateEmployeeCommand command) {
        // CQRS
        return employeeService.saveEmployee(command.getName());
    }

    @GetMapping("/{id}")
    public Employee findEmployeeById(@PathVariable long id) {
        return employeeService.findEmployeeById(id);
    }
}
