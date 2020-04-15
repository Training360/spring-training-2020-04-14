package training;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FirstModuleMenuProvider implements MenuProvider {

    @Override
    public List<String> getMenuItems() {
        return List.of("Module 1 Menu 1", "Module 1 Menu 2");
    }
}
