package training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "/application-context.xml")
public class EmployeeServiceXmlIT {

    @Autowired
    private EmployeeDao dao;

    @Autowired
    private EmployeeService service;

    @Test
    public void testSave() {
        service.saveEmployee("John Doe");
        List<String> names = dao.getEmployees();
        assertEquals(List.of("JOHN DOE"), names);
    }
}
