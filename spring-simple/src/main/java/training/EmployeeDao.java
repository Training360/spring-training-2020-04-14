package training;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeDao {

    private List<String> employees = Collections.synchronizedList(new ArrayList<>());

    public void saveEmployee(String name) {
        System.out.println("Save employee");
        employees.add(name);
    }

    public List<String> getEmployees() {
        return new ArrayList<>(employees);
//        return Collections.unmodifiableList(employees);
    }

    public void emptyEmployees() {
        employees.clear();
    }
}
