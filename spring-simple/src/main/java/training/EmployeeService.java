package training;

public class EmployeeService {

    private final EmployeeDao employeeDao;

    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public void saveEmployee(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name can not be empty");
        }
        var upperCase = name.toUpperCase();
//        var upperCase = name;
//        employeeDao.getEmployees();
        employeeDao.saveEmployee(upperCase);
    }
}
