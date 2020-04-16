package training;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeDao {

    private JdbcTemplate jdbcTemplate;

    public EmployeeDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static Employee toEmployee(ResultSet resultSet, int i) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("emp_name");
        return new Employee(id, name);
    }

    public Employee saveEmployee(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement("insert into employees(emp_name) values (?)",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder
        );
        var id = keyHolder.getKey().longValue();

        return new Employee(id, name);
    }

    public List<Employee> getEmployees() {
        return jdbcTemplate.query("select id, emp_name from employees",
                EmployeeDao::toEmployee);
    }

    public Employee findEmployeeById(long id) {
        return jdbcTemplate.queryForObject("select id, emp_name from employees where id = ?",
                new Object[]{id}, EmployeeDao::toEmployee);
    }

    public Employee updateEmployee(long id, String name) {
        jdbcTemplate.update("update employees set emp_name = ? where id = ?",
                name, id);
        return new Employee(id, name);
    }

    public void deleteEmployee(long id) {
        jdbcTemplate.update("delete from employees where id = ?", id);
    }

    public void emptyEmployees() {

        jdbcTemplate.update("delete from employees");
    }
}
