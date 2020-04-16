package training;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Employee saveEmployee(String name) {
        var employee = new Employee(name);
        em.persist(employee);
        return employee;
    }

    public List<Employee> getEmployees() {
        return em.createQuery("select e from Employee e", Employee.class).getResultList();
    }

    public Employee findEmployeeById(long id) {
        return em.find(Employee.class, id);
    }

    public Employee updateEmployee(long id, String name) {
        var employee = em.find(Employee.class, id);
        employee.setName(name);
        return employee;
    }

    public void deleteEmployee(long id) {
        var employee = em.find(Employee.class, id);
        em.remove(employee);
    }

    public void emptyEmployees() {
        em.createQuery("delete from Employee e").executeUpdate();
    }
}
