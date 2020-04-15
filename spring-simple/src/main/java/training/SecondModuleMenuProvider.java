package training;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SecondModuleMenuProvider implements MenuProvider {

    @Override
    public List<String> getMenuItems() {
        return List.of("Module 2 Menu 1", "Module 2 Menu 2");
    }
}
