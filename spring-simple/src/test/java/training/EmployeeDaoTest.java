package training;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmployeeDaoTest {

    @Test
    public void testSaveEmployee() {
        var dao = new EmployeeDao();
        dao.saveEmployee("John Doe");

        assertEquals(List.of("John Doe"), dao.getEmployees());
    }
}
