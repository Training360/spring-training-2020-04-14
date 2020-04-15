package training;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

@Component
public class SpringInspector {

    private List<Object> beans;

    public SpringInspector(List<Object> beans) {
        this.beans = beans;
    }

    @PostConstruct
    public void init() {
        System.out.println("SpringInspector has been started");
        System.out.println(Arrays.asList(beans));
    }
}
