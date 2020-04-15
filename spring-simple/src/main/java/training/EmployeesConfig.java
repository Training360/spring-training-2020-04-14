package training;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan
public class EmployeesConfig {

//    @Bean
//    public EmployeeDao employeeDao() {
//        System.out.println("Create employee dao");
//        return new EmployeeDao();
//    }

//    @Bean // (initMethod = "init")
//    public EmployeeService employeeService(ApplicationContext applicationContext) {
//        var service = new EmployeeService(employeeDao(), applicationContext);
//        service.init();
//        return service;
//    }

//    @Bean
//    @Scope("prototype")
//    public Employee defaultEmployee() {
//        return new Employee("John");
//    }

    @Bean
    public String name() {
        return "John";
    }

}
