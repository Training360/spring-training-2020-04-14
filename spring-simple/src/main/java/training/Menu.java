package training;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;

@Component
public class Menu {

    List<MenuProvider> menuProviders;

    public Menu(List<MenuProvider> menuProviders) {
        this.menuProviders = menuProviders;
    }

    @PostConstruct
    public void printMenu() {
        System.out.println("Print menu");
//        for (var provider: menuProviders) {
//            var items = provider.getMenuItems();
//            for (String item: items) {
//                System.out.println(item);
//            }
//        }
        menuProviders
                .stream()
                .map(MenuProvider::getMenuItems)
                .flatMap(Collection::stream)
                .filter(s -> s.contains("Menu 2"))
                .forEach(System.out::println);
    }
}
