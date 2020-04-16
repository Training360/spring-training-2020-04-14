package training;

import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.SQLException;
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

    private DataSource dataSource;

    public EmployeeDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Employee saveEmployee(String name) {
        var employee = new Employee(sequence.getAndIncrement(), name);
        employees.add(employee);

        try (var conn = dataSource.getConnection();
        var ps = conn.prepareStatement("insert into employees(emp_name) values (?)")) {
            ps.setString(1, name);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw new IllegalStateException("Cannot insert", e);
        }

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
