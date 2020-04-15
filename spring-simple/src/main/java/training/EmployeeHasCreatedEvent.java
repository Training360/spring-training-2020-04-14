package training;

import org.springframework.context.ApplicationEvent;

public class EmployeeHasCreatedEvent extends ApplicationEvent {

    private String name;

    public EmployeeHasCreatedEvent(Object source, String name) {
        super(source);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
