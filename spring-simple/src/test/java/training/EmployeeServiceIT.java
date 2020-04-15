package training;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmployeesConfig.class)
public class EmployeeServiceIT {

    @Autowired
    private EmployeeDao dao;

    @Autowired
    private EmployeeService service;

//    @BeforeEach
//    public void emptyEmployees() {
//        dao.emptyEmployees();
//    }

    @Test
//    @DirtiesContext
    public void testSave() {
        service.saveEmployee("John Doe admin ");
        service.saveEmployee("Jack Doe");
        List<String> names = dao.getEmployees();
        assertEquals(List.of("JOHN DOE", "JACK DOE"), names);
    }

//    @Test
//    @DirtiesContext
//    public void testSave2() {
//        service.saveEmployee("Jack Doe");
//        List<String> names = dao.getEmployees();
//        assertEquals(List.of("JACK DOE"), names);
//    }
}
