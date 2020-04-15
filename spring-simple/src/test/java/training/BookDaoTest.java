package training;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
//@ActiveProfiles({"jpa", "jdbc"})
public class BookDaoTest {

    @Autowired
    BookDao bookDao;

    @Configuration
    @PropertySource("classpath:application.properties")
    static class Config {

        @Bean
//        @Profile("jdbc")
        @Conditional(JdbcCondition.class)
        public BookDao jdbcBookDao() {
            return new JdbcBookDao();
        }

        @Bean
//        @Profile("jpa")
        @Conditional(JpaCondition.class)
        public BookDao jpaBookDao() {
            return new JpaBookDao();
        }

    }

    @Test
    void testSaveBook() {
        bookDao.saveBook("Hoeller", "Spring...");
    }
}
