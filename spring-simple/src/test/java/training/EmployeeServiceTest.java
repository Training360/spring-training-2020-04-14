package training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeDao dao;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private EmployeeService service;

    @Test
    public void testSaveEmployeeWithEmpty() {
        assertThrows(IllegalArgumentException.class, () -> service.saveEmployee("   "));
    }

    @Test
    public void testSaveEmployee() {
        service.saveEmployee("John Doe");

        verify(dao).saveEmployee(eq("JOHN DOE"));
        verify(dao, never()).getEmployees();
    }
}
