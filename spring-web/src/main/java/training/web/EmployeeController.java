package training.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import training.backend.EmployeeService;

@Controller
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public ModelAndView listEmployees() {
        var employees = employeeService.listEmployees();
        return new ModelAndView("employees-page", "employeesList", employees);
    }
}
