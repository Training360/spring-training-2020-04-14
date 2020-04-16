class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
# Alkalmazásfejlesztés Spring keretrendszerrel - Adatbáziskezelés

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Tematika

---

## Tematika

* Spring Framework repository réteg
* Séma inicializálás Flyway eszközzel
* Spring JdbcTemplate
* JPA használata Spring Frameworkkel
* Spring Data JPA
* Deklaratív tranzakciókezelés

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
##  Spring Framework repository réteg

---

## Relációs adatbázishoz való hozzáférés

* JDBC Driver implementálja a JDBC API-t

```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.41</version>
</dependency>
```

---

## JDBC url

* Adatbázis szerver elérésének megadására, Java specifikus
* `jdbc:[gyártó]:[adatbázis specifikus elérési út]`  
* `jdbc:mysql://localhost:3306/employees?useUnicode=true`
* `jdbc:oracle:thin:@localhost:1521:employees`
* `jdbc:sqlserver://localhost\emplyoees`
* `jdbc:h2:mem:db;DB_CLOSE_DELAY=-1`

---

## DataSource

* Connection factory
* Connection pool
* Közvetlenül is példányosítható
* Beanként deklarálandó

---

## DAO

* Műveletek DAO-ban (Data Access Object - Java EE tervezési minta)
* DataSource hozzáférés DI-vel

---

## DAO implementáció

```java
@Repository
public class EmployeeDao {

  private DataSource dataSource;

  public EmployeeDao(DataSource dataSource) {
        this.dataSource = dataSource;
  }

  public void saveEmployee(String name) {
    Connection conn = dataSource.getConnection();    
    // ...
  }

  public List<String> listEmployeeNames() {
      Connection conn = dataSource.getConnection();    
      // ...
  }
}
```

---

## Konfiguráció

```java
@Configuration
@ComponentScan(basePackageClasses = EmployeeDao.class)
public class AppConfig {

  @Bean
  public DataSource dataSource() {
    MysqlDataSource dataSource = new MysqlDataSource();
    dataSource.setUrl("jdbc:mysql://localhost:3306/employees?useUnicode=true");
    return dataSource;
  }

}
```

---

## Konfiguráció kiszervezése

* `@PropertySource` és `Environment` használatával

---

## Integrációs tesztelés

* Teszteset maga készítse elő a szükséges adatokat
* Teszteset tegyen rendet maga után
* Rollback
* Adatbázis takarítás
  * Séma létrehozás
  * Táblák ürítése (truncate, megszorítások)

---

## Integrációs teszt példa

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Sql(scripts = "classpath:/clear.sql")
public class EmployeeDaoTest {

    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void testInsertThanQuery() {
        employeeDao.saveEmployee("John Doe");

        List<String> names = employeeDao.listEmployeeNames();
        assertThat(names, equalTo(Arrays.asList("John Doe")));
    }

}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Séma inicializálás Flyway eszközzel

---

## Séma inicializálás

* Adatbázis séma létrehozása (táblák, stb.)
* Változások megadása
* Metadata table alapján  

---

## Elvárások

* SQL/XML leírás
* Platform függetlenség
* Lightweight
* Visszaállás korábbi verzióra
* Indítás paranccssorból, alkalmazásból
* Cluster támogatás
* Placeholder támogatás
* Modularizáció
* Több séma támogatása

---

## Flyway függőség

```xml
<dependency>
  <groupId>org.flywaydb</groupId>
  <artifactId>flyway-core</artifactId>
  <version>5.2.3</version>
</dependency>
```

---

## Séma inicializálás Flyway eszközökkel

```java
@Bean
public Flyway flyway() {
    Flyway flyway = Flyway.configure()
        .dataSource(dataSource()).load();

    flyway.migrate();
}
```

`V1__employees.sql` állomány

```sql
create table employees (id bigint auto_increment,
  emp_name varchar(255),
    constraint pk_employee primary key (id));
```

`flyway_schema_history` tábla

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Spring JdbcTemplate

---

## `JdbcTemplate`

* JDBC túl bőbeszédű
* Elavult kivételkezelés
  * Egy osztály, üzenet alapján megkülönböztethető
  * Checked
* Spring Framework: Inversion of Control framework
* Boilerplate kódok eliminálására template-ek

---

## Módosítás

```java
public void saveEmployee(String name) {
    jdbcTemplate.update("insert into employee(emp_name) values ('John Doe')");
}
```

---

## Paraméterezett módosítás

```java
public void saveEmployee(String name) {
    jdbcTemplate.update("insert into employee(emp_name) values (?)", name);
}
```

---

## Lekérdezés

```java
public List<String> listEmployeeNames() {
    return jdbcTemplate.query("select emp_name from employees", new RowMapper<String>() {
        @Override
        public String mapRow(ResultSet rs, int rowNum) throws SQLException {
            return rs.getString(1);
        }
    });
}
```

---

## Lekérdezés lambda kifejezéssel

```java
public List<String> listEmployeeNames() {
    return jdbcTemplate.query("select emp_name from employee", (rs, i) -> rs.getString(1));
}
```

---

## Lekérdezés lambda kifejezéssel, paraméterezetten

```java
public List<String> listEmployeeNames(String prefix) {
    return jdbcTemplate.query("select emp_name from employee where emp_name like ?",
      new Object[]{prefix + "%"}, (rs, i) -> rs.getString(1));
}
```

---

## Lekérdezés egy objektumra, paraméteresen

```java
public String findEmployeeNameById(long id) {
    return jdbcTemplate.queryForObject("select emp_name from employees where id = ?",
            new Object[]{id}, String.class);
}
```

---

## Id visszakérése

```java
public long saveEmployeeAndGetId(String name) {
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
              throws SQLException {
                PreparedStatement ps =
                    connection.prepareStatement("insert into employees(emp_name) values (?)",
                      Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                return ps;
            }
        }, keyHolder
    );

    return keyHolder.getKey().longValue();
}
```

---

## Id visszakérése lambda kifejezéssel

```java
public long saveEmployeeAndGetId(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement("insert into employees(emp_name) values (?)",
                        Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, name);
            return ps;
        }, keyHolder
        );

        return keyHolder.getKey().longValue();
    }
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## JPA használata Spring Frameworkkel

---

## JPA

* JDBC bonyolultsága: leképzés a relációs adatbázis és oo világ között
* Megoldás: keretrendszer biztosítsa konfiguráció alapján
* ORM: Object-Relational Mapping
* Szabvány: JPA
* Implementációi: Hibernate, EclipseLink, OpenJPA
* JDBC-re épül

---

## Függőségek

```xml
<dependency>
  <groupId>org.hibernate</groupId>
  <artifactId>hibernate-entitymanager</artifactId>
  <version>5.2.18.Final</version>
</dependency>
```

---

## Entitás

```java
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "emp_name")
    private String name;

    // Getter és setter metódusok
}
```

```sql
create table employees (id bigint auto_increment,
  emp_name varchar(255),
    constraint pk_employees primary key (id));
```

---

## DAO

```java
@Repository
public class EmployeeDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveEmployee(Employee employee) {
        entityManager.persist(employee);
    }

    public Employee findEmployeeById(long id) {
        return entityManager.find(Employee.class, id);
    }
}
```

---

## Spring JPA integráció

* `EntityManagerFactory` definiálása beanként
* `persistence.xml` nem szükséges
* Deklaratív tranzakciókezelés

```xml
<dependency>
  <groupId>org.springframework</groupId>
  <artifactId>spring-orm</artifactId>
</dependency>
```

---

## Konfiguráció

```java
@Bean
public JpaTransactionManager transactionManager() {
    return new JpaTransactionManager();
}

@Bean
public JpaVendorAdapter jpaVendorAdapter() {
  HibernateJpaVendorAdapter hibernateJpaVendorAdapter =
          new HibernateJpaVendorAdapter();
  hibernateJpaVendorAdapter.setShowSql(true);
  return hibernateJpaVendorAdapter;
}
```

---

## Konfiguráció

```java
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
      LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
              new LocalContainerEntityManagerFactoryBean();
      entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());
      entityManagerFactoryBean.setDataSource(dataSource);
      entityManagerFactoryBean.setPackagesToScan("spring.di");
      return entityManagerFactoryBean;
}
```

---

## Deklaratív tranzakciókezelés

DAO-n:

```java
@Transactional
```

`@Configuration` annotációval ellátott osztályon:

```java
@EnableTransactionManagement
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Spring Data JPA

---

## Spring Data JPA

* Egyszerűbbé teszi a perzisztens réteg implementálását
* Tipikusan CRUD műveletek támogatására, olyan gyakori igények megvalósításával, mint a rendezés és a lapozás
* Interfész alapján repository implementáció generálás
* Ismétlődő fejlesztési feladatok redukálása, *boilerplate* kódok csökkentése

---

## Maven függőség

```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-jpa</artifactId>
    <version>2.1.2.RELEASE</version>
</dependency>
```

---

## Konfiguráció

* `@Configuration` annotációval ellátott osztályban

```java
@EnableJpaRepositories
```

---

## CrudRepository

```java
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
```
A következő metódusokat definiálja: `save(Employee)`, `saveAll(Iterable<Employee>)`, `findById(Long)`, `existsById(Long)`, `findAll()`, `findAllById(Iterable<Long>)`, `count()`, `deleteById(Long)`, <br /> `delete(Employee)`, `deleteAll()`, <br /> `deleteAll(Iterable<Employee>)`

---

## Teszt eset CrudRepository-ra

Példa teszt eset:

```java
@Test
public void testSaveThenFindAll() {
    employeeRepository.save(new Employee("John Doe"));

    Iterable<Employee> employees = employeeRepository.findAll();
    assertEquals(List.of("John Doe"), StreamSupport.stream(employees.spliterator(), false)
      .map(Employee::getName)
      .collect(Collectors.toList()));
}
```

---

## PagingAndSortingRepository

```java
public interface PagingAndSortingRepository<T, ID extends Serializable>
  extends CrudRepository<T, ID> {

  Iterable<T> findAll(Sort sort);

  Page<T> findAll(Pageable pageable);
}
```

```java
Sort.by("name")
Sort.by("name").descending()
PageRequest.of(1, 20)
PageRequest.of(1, 20, Sort.by("name"))
```

---

## Page

* `int getNumberOfElements()`
* `List<T> getContent()`
* `int getTotalPages()`
* `long getTotalElements()`
* `boolean isFirst()`, `boolean isLast()`, `boolean hasNext()`, `boolean hasPrevious()`, <br /> `Pageable getPageable()`, <br /> `Pageable nextPageable()`, <br /> `Pageable previousPageable()`

---

## Lekérdező metódusok

* [Query Creation](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation)


```java
interface EmployeeRepository extends Repository<Employee, Long> {

  List<Employee> findByName(String name);

  List<Employee> findDistinctEmployeeByNameOrEmail(String name, String email);

  List<Employee> findByNameIgnoreCase(String name);

  List<Employee> findByNameOrderByNameAsc(String name);

}
```

* Visszatérés: `List<T>`, `Iterable<T>`, `Stream<T>`
* `Stream<T>` lezárandó, érdemes try-with-resources <br /> szerkezetben

---

## Metódus definiálása JPA query-vel

Query annotáció használatával

```java
@Query("select e from Employee e where length(e.name) = :nameLength")
Iterable<Employee> findByNameLength(@Param("nameLength") int nameLength);
```

* Megadható count query `countQuery` paraméterként
* Megadható native query `nativeQuery = true` paraméter esetén

---

## Saját implementáció - interfész

Saját interfész:

```java
public interface CustomizedEmployeeRepository {

    List<Employee> findByNameStartingWithAsList(String namePrefix);
}
```

---

## Saját implementáció - implementáció

```java
public class CustomizedEmployeeRepositoryImpl implements CustomizedEmployeeRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Employee> findByNameStartingWithAsList(String namePrefix) {
        return entityManager
            .createQuery(
                "select e from Employee e where e.name like :namePrefix",
                Employee.class)
            .setParameter("namePrefix", namePrefix + "%").getResultList();
    }
}
```

A saját implementáció neve kötelezően a repository <br /> interfész neve
az `Impl` posztfixszel


---

## Saját implementáció - Repository interfész

Interfész kiterjessze az új interfészt is:

```java
public interface EmployeeRepository
    extends CrudRepository<Employee, Long>, CustomizedEmployeeRepository {
        // ...
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
# Deklaratív tranzakciókezelés

---

## Tranzakciókezelés

![Tranzakciókezelés](images/tranzakcio-kezeles.png)

---

## Propagáció

![Propagáció](images/propagacio.png)

---

## Propagációs tulajdonságok

* `REQUIRED` (default): ha nincs tranzakció, indít egyet, ha van csatlakozik hozzá
* `REQUIRES_NEW`: mindenképp új tranzakciót indít
* `SUPPORTS`: ha van tranzakció, abban fut, ha nincs, nem indít újat
* `MANDATORY`: ha van tranzakció, abban fut, ha nincs, kivételt dob
* `NOT_SUPPORTED`: ha van tranzakció, a tranzakciót felfüggeszti, ha nincs, nem indít újat
* `NEVER`: ha van tranzakció, kivételt dob, ha nincs, <br /> nem indít újat

---

## Izoláció

* Izolációs problémák:
    * dirty read
    * non-repetable read
    * phantom read
* Izolációs szintek:
    * read uncommitted
    * read commited 
    * repeatable read
    * serializable

---

## Visszagörgetési szabályok

* Kivételekre lehet megadni, hogy melyik esetén történjen rollback
* Rollbackre explicit módon megjelölni
* Konténer dönt a commitról vagy rollbackről

---

## Timeout

* Timeout esetén kivétel

---

## Csak olvasható

* Spring esetén további optimalizációkat tud elvégezni, cache-eléssel kapcsolatos