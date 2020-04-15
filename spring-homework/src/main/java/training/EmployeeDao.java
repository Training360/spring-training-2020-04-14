package training;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class EmployeeDao {

    private List<Employee> employees = Collections.synchronizedList(new ArrayList<>());

    private AtomicLong sequence = new AtomicLong();

    public Employee saveEmployee(String name) {
        var employee = new Employee(sequence.getAndIncrement(), name);
        employees.add(employee);
        return employee;
    }

    public List<Employee> getEmployees() {
        return new ArrayList<>(employees);
    }

    public Employee findEmployeeById(long id) {
        return employees.stream().filter(e -> e.getId()== id).findFirst().orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    public Employee updateEmployee(long id, String name) {
        var employee = findEmployeeById(id);
        employee.setName(name);
        return employee;
    }

    public void deleteEmployee(long id) {
        employees = employees.stream().filter(e -> e.getId() != id).collect(Collectors.toList());
    }

    public void emptyEmployees() {
        employees.clear();
    }
}
