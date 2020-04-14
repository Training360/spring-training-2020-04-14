class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
# Alkalmazásfejlesztés Spring keretrendszerrel - Spring Framework IoC container

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Tematika

---

## Tematika 1.

* Bevezetés a Spring Framework használatába
* Inversion of Control és Dependency Injection
* Unit és integrációs tesztelés Spring környezetben
* Beanek személyre szabása
* Konfiguráció XML-lel és annotációval
* Injektálás konfigurálása

---

## Tematika 2.

* Eseménykezelés
* Konfigurációs állományok
* Profile
* Conditional beans
* Naplózás
* Aspektusorientált programozás


---

## Források

* [Craig Walls: Spring in Action](https://www.amazon.com/Spring-Action-Craig-Walls/dp/1617294942) (5. kiadás)

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
##  Bevezetés a Spring Framework használatába

---

## Spring Framework céljai

* Keretrendszer nagyvállalati alkalmazásfejlesztésre
* Keretrendszer: komponensek hívása, életciklusa
* Nagyvállalati alkalmazás: Java SE által nem támogatott tulajdonságok
    * Spring Framework nem magában ad választ, hanem integrál egy vagy több megoldást

---

class: split-50

## Nagyvállalati alkalmazás

<div>
.column[
* Perzisztencia
* Többszálúság
* Tranzakció-kezelés
* Távoli elérés
* Névszolgáltatás
]
.column[
* Skálázhatóság
* Magas rendelkezésre állás
* Aszinkron üzenetkezelés
* Biztonság
* Monitorozás és beavatkozás
]
</div>

---

## Spring keretrendszer tulajdonságai

* Pehelysúlyú, non invasive
* POJO-kra épülő komponens modell, melyeket konténerként tartalmaz
* Többrétegű alkalmazás architektúra
* Dependency Injection, Inversion of Control
* Aspektusorientált programozás támogatása
* 3rd party library-k integrálása az egységes modellbe
* Glue kód
* Boilerplate kódok eliminálása
* Fejlesztők az üzleti problémák <br /> megoldására koncentráljanak

---

## Háromrétegű webes alkalmazás

* Nem kizárólag erre, de ez a fő felhasználási terület
* Rétegek
    * Repository
    * Service
    * Controller
* HTTP kezelését web konténerre bízza, <br /> pl. Jetty, Tomcat, stb.

---

## Spring bean, POJO

* Egyszerű Java osztály
* Nem kell interfészt implementálni
* Nem kell egy osztálytól leszármazni
* Nem kell annotációval ellátni
* Nem kell elnevezési konvenciókat betartani
* Nincsenek keretrendszerbeli függőségei

---

## Egyszerű Spring bean

* Spring konténer által felügyelt példányok
* Tipikusan a példányosítást és a függőségek beállítását a Spring konténer végzi
* Egy konfiguráció alapján történik a példányosítás és a függőségek beállítása

```java
public class EmployeeDao {

  public void saveEmployee(String name) {
    // ...
  }
}
```

---

## Másik beanre hivatkozó Spring bean

```java
public class EmployeeService {

  private final EmployeeDao employeeDao;

  public EmployeeService(EmployeeDao employeeDao) {
    this.employeeDao = employeeDao;
  }
}
```

---

## JDBC Spring nélkül

```java
public class EmployeeDao {

  private DataSource dataSource;

  public EmployeeDao(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  public void saveEmployee(String name) {
    try (Connection conn = dataSource.getConnection();
      PreparedStatement stmt = conn.prepareStatement("insert into employees(emp_name) values (?)");
    ) {
      stmt.setString(1, name);
      stmt.execute();
    } catch (SQLException sqle) {
      throw new IllegalArgumentException("Error by insert", sqle);
    }
  }
}
```

---

## JDBC Spring használatával

```java
public class EmployeeDao {

  private JdbcTemplate jdbcTemplate;

  public EmployeeDao(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void saveEmployee(String name) {
    jdbcTemplate.update("insert into employee(emp_name) values (?)", name);
  }

}
```

---

## AOP

```java
@Aspect
public class CounterAspect {

	private AtomicInteger count = new AtomicInteger();

	@Before("execution(* spring.EmployeeDao.saveEmployee(..))")
	public void inc() {
		count.incrementAndGet();
	}

	public int getCount() {
		return count.get();
	}
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
##  Inversion of Control és Dependency Injection

---

## Inversion of Control

* A program részei egy újrafelhasználható keretrendszertől kapják meg a vezérlést
* Komponensek és konténer
* Növeli a modularitást és a kiterjeszthetőséget
* Robert C. Martin és Martin Fowler terjesztette el
* "Don't call us we will call you"

---

## Dependency Injection

* Tervezési minta (nem GoF)
* Inversion of Control egy implementációja
* Az objektumok nem maguk példányosítják vagy kérik le a függőségeiket
* Csak leírják, mire van szükségük
* A komponenseket a konténer példányosítja és köti össze
* Egy központosított vezérlés a komponensek <br /> életciklusa és kapcsolatai fölött

---

## Dependency Injection előnyei

* Low coupling (laza kapcsolat)
* Implementációk cserélhetősége
* Tesztelés segítése (test double objektumok használata: pl. dummy, stub, mock)
* Újrafelhasználhatóság növelése, boilerplate kód csökkentése

---

## Másik beanre hivatkozó bean

```java
public class EmployeeService {

  private EmployeeDao employeeDao;

  public EmployeeService() {
    this.employeeDao = new EmployeeDao();
  }
}
```

---

## Másik beant lekérdező bean

```java
public class EmployeeService {

  private final EmployeeDao employeeDao;

  public EmployeeService() {
    try {
      Context initCtx = new InitialContext();
      Context envCtx = (Context) initCtx.lookup("java:comp/env");
      this.employeeDao = (EmployeeDao) envCtx.lookup("ejb/EmployeeDao");
    }
    catch (NamingException ne) {
      throw new IllegalStateException("Cannot get EmployeeDao bean", ne);
    }
  }
}
```

---

## Spring függőségek

* BOM: Bill of materials

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-framework-bom</artifactId>
            <version>${spring.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
    </dependency>
</dependencies>
```

---

## Másik beanre hivatkozó Spring bean

```java
public class EmployeeService {

  private final EmployeeDao employeeDao;

  public EmployeeService(EmployeeDao employeeDao) {
    this.employeeDao = employeeDao;
  }

  public boolean saveEmployee(String name) {
    String trimmedName = name.trim();
    employeeDao.saveEmployee(trimmedName);
  }
}
```

---

## Injektálás fajtái

* Konstruktor injection
  * Kötelező függőségek
  * Unit tesztelésnél, példányosításkor kötelező megadni
* Setter injection
  * Opcionális függőségeknél
* Attribute injection
* Keverhetők

---

## Konfiguráció

```java
@Configuration
public class AppConfig {

	@Bean
	public EmployeeDao employeeDao() {
		return new EmployeeDao();		
	}

	@Bean
	public EmployeeService employeeService() {
		return new EmployeeService(employeeDao());
	}
}
```

---

## @Bean annotáció

* Beanek létrehozására hívja meg az annotációval ellátott metódusokat
* A bean neve a metódus neve
* Az annotáció paramétereként megadható más név (nevek) is

```java
@Bean({"employeeRepository", "employeeDao"})
public EmployeeDao employeeDao() {
  return new EmployeeDao();		
}
```
---

## @Configuration annotáció

* CGLIB leszármazatja, így biztosítja, hogy a megfelelő metódusok csak egyszer legyenek meghívva
* Megvizsgálja, hogy a bean létezik a konténerben, ha létezik, visszaadja, amúgy példányosítja, elhelyezi és visszaadja

Spring Framework-nek a 3.2 verzió óta a CGLIB része, `org.springframework` csomaggal bele van csomagolva

---

## Injektálás config osztályba

```java
@Configuration
public class AppConfig {

  @Autowired
  private EmployeeDao employeeDao;

	@Bean
	public EmployeeService employeeService() {
		return new EmployeeService(employeeDao);
	}
}
```

---

## Injektálás paraméterként

```java
@Configuration
public class AppConfig {

	@Bean
	public EmployeeService employeeService(EmployeeDao employeeDao) {
		return new EmployeeService(employeeDao);
	}
}
```

---

## Spring indítása

* Spring konténer: application context

```java
try (AnnotationConfigApplicationContext ctx =
	new AnnotationConfigApplicationContext(AppConfig.class)) {
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Unit és integrációs tesztelés Spring környezetben

---

## Unit tesztelés

* Független a Springtől

```java
public class CalculatorTest {

  private Calculator calculator = new Calculator();

  public void testCalculate() {    
    int result = calculator.add(1, 1);
    assertEquals(2, result);
  }

}
```

---

## Unit tesztelés függőséggel

```java
public class EmployeeServiceTest {

    private EmployeeDao employeeDao = Mockito.mock(EmployeeDao.class);

    private EmployeeService employeeService = new EmployeeService(employeeDao);

    @Test
    public void testSaveEmployee() {
        employeeService.saveEmployee("  John Doe  ");
        verify(employeeDao).saveEmployee("John Doe");
    }
}
```

---

## Unit tesztelés függőséggel injektálással

```java
@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceTestInject {

    @Mock
    private EmployeeDao employeeDao;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    public void testSaveEmployee() {
        employeeService.saveEmployee("  John Doe  ");
        verify(employeeDao).saveEmployee("John Doe");
    }
}
```

---

## Integrációs tesztelés application context létrehozással

```java
public class EmployeeServiceIntegrationTest {

    @Test
    public void testSaveAndList() {
        AnnotationConfigApplicationContext ctx =
                new AnnotationConfigApplicationContext(AppConfig.class);
        EmployeeService employeeService = ctx.getBean(EmployeeService.class);

        employeeService.saveEmployee("John Doe");
        assertEquals(List.of("John Doe"), employeeService.listEmployees());
    }
}
```

---

## Integrációs tesztelés Spring runnerrel

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class EmployeeServiceRunnerIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @Test
    public void testSaveAndList() {
        employeeService.saveEmployee("John Doe");
        assertEquals(List.of("John Doe"), employeeService.listEmployees());
    }
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Beanek személyre szabása

---

## Bean létrehozás sorrendje

* Konténer indítás két fázisban:
  * Konfiguráció (metaadatok) betöltése
  * Konkrét bean létrehozás
* Először felderíti a függőségeket, és annak megfelelően

---

## Sorrend testreszabása

* `@DependsOn` annotációval

---

## Lazy beanek

* Alapesetben eager bean létrehozás: konténer induláskor létrehozza
* Konfigurálható a `@Lazy` annotáció használatával
* A `@Bean` annotációval ellátott metódusra

---

## Körkörös injectálás

* Két bean egymásra hivatkozik
* Kerüljük
* Két konstruktor injection esetén hibaüzenet

---

## Bean scope

* singleton (alapértelmezett): egy az application contextben
* prototype: annyiszor létrehozott, ahányszor használt
* request: http kérésenként egy
* session: http sessionönként egy
* global-session: portlet környezetben global sessionönként egy
* `@Scope` annotációval

---

## Bean életciklus

* Példányosítás
* Injection
* Init metódusok
* Használatra kész
* Destroy metódusok
* Megszűnt

---

## Init és destroy metódusok

* `@Bean` annotáció paramétereként
* Init metódus `@Bean` annotációval ellátott metódusban meghívható
* `@PostConstruct` annotációval ellátott metódusok
* `@PreDestroy` annotációval ellátott metódusok

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Konfiguráció XML-lel és annotációval

---

## Java konfiguráció

```java
@Configuration
public class AppConfig {

	@Bean
	public EmployeeDao employeeDao() {
		return new EmployeeDao();		
	}

	@Bean
	public EmployeeService employeeService() {
		return new EmployeeService(employeeDao());
	}
}
```

```java
try (AnnotationConfigApplicationContext ctx =
	new AnnotationConfigApplicationContext(AppConfig.class)) {
}
```

---

## XML Konfiguráció

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd">

	<bean id="employeeDao" class="spring.di.EmployeeDao" />

	<bean id="employeeService" class="spring.di.EmployeeService">
		<constructor-arg ref="employeeDao" />
	</bean>

</beans>
```

---

## XML betöltése

```java
try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
				"/application-context.xml")) {
}
```

---

## Component scan

* `@ComponentScan` annotáció, megadható csomag
* Stereotype-ok
  * `@Component`: általános célú bean
  * `@Controller`: Spring MVC controller
  * `@Service`: üzleti logikáért felelős bean
  * `@Repository`: perzisztenciáért, <br /> adatbázis hozzáférésért felelős bean
  * Saját is készíthető

---

## Beanek annotációval

```java
@Repository
public class EmployeeDao {
}
```

```java
@Service
public class BookService {
}
```

* Neve az osztály neve, első betű kisbetű
* Felülbírálható az annotációk paraméterével megadva

---

## Scan filterelése

* `annotation`: annotáció alapján
* `assignable`: típus alapján
* `aspectj`: AspectJ kifejezés alapján
* `custom`: Javaban implementálható
* `regexp`: reguláris kifejezés alapján

---

## Függőség

* `@Autowired` annotáció

---

## Annotáció konstruktoron

* Injektálás típus alapján

```java
public class EmployeeService {

    @Autowired
    public EmployeeService(EmployeeDao employeeDao) {
        // ...
    }

}
```

---

## Konstruktor injektálás annotáció nélkül

* Spring Framework 4.3 óta egy konstruktor esetén nem szükséges kitenni

```java
public class EmployeeService {

    public EmployeeService(EmployeeDao employeeDao) {
        // ...
    }

}
```

---

## Annotáció setteren

```java
public class EmployeeService {

    @Autowired
    public void setEmployeeDao(EmployeeDao employeeDao) {
        // ...
    }

}
```

---

## Annotáció attribútumon

```java
public class EmployeeService {

    @Autowired
    private EmployeeDao employeeDao;

}
```

---

## Konfigurációk összehasonlítása

* XML: non-invasive, központi konfiguráció
* Annotációk: invasive, elosztott konfiguráció
* Java: non-invasive, központi, típusbiztos

---

## Konfigurációk keverhetősége

* Component scan felolvassa a `@Configuration` annotációval ellátott osztályokat is

```java
@Configuration
@Import(SecurityConfig.class)
public class AppConfig {

  // ...

}
```

```java
@Configuration
@ImportResource("classpath:/security-application-context.xml")
public class AppConfig {
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Injektálás konfigurálása

---

## Több bean ugyanazzal a típussal

* Pl. ha interfész (ritkábban ősosztály) alapján történik az injektálás
* Az interfésznek több implementációja van az application contextben

---

## Több bean ugyanazzal a típussal

```
nested exception is org.springframework.beans.factory.BeanCreationException: Could not autowire field:
spring.di.EmployeeDao spring.di.EmployeeService.employeeDao;
nested exception is org.springframework.beans.factory.NoSuchBeanDefinitionException:
No unique bean of type [spring.di.EmployeeDao] is defined:
expected single matching bean but found 2: [employeeDao, dummyEmployeeDao]
```

* `@Primary` annotációval megjelölni az elsődleges beant

---

## @Qualifier annotáció használata

* `@Qualifier` annotációval ellátható egy bean
* `@Qualifier` annotációval meg lehet adni a nevét az injektálás helyén

```java
@Configuration
public class AppConfig {

    // Más EmployeeDao implementációk

    @Bean
    @Qualifier("employeeDao")
    public EmployeeDao jdbcEmployeeDao() {
    	return new JdbcEmployeeDao();		
    }

    @Bean
    public EmployeeService employeeService
          (@Qualifier("employeeDao") EmployeeDao employeeDao) {
    	return new EmployeeService(employeeDao);
    }
}
```

---

## Saját qualifier

* Saját annotáció `@Qualifier` annotációval annotálva

```java
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface JdbcPersistence {
}
```

---

## Saját qualifier használata

```java
@JdbcPersistence
public class JdbcEmployeeDao implements EmployeeDao {
...
}
```

```java
@Autowired
@JdbcPersistence
private EmployeeDao employeeDao;
```

* Egy osztályon több qualifier is használható

---

## Opcionális injektálás

* Ha nem található: `NoSuchBeanDefinitionException`

```java
public class EmployeeService {

    @Autowired(required = false)
    public void setEmployeeDao(EmployeeDao employeeDao) {
        // ...
    }

}
```

---

## Összes implementáció injektálása

```java
@Autowired
private List<EmployeeDao> allEmployeeDaos;
```

---

## Application context injektálása

```java
@Autowired
private ApplicationContext ctx;
```

---

## Alternatív szabványok

* JSR-330 szabvány `@Inject` annotációja
* JSR-250 szabvány `@Resource` annotációja

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Eseménykezelés

---

## Eseménykezelés

* Laza kapcsolat a komponensek között
* Küldő és fogadó nem tud egymástól
* Több fogadó is lehet
* Egy fogadó több eseményre is feliratkozhat

---

## Mechanizmus

* Eseményeknek ki kell terjeszteniük a `ApplicationEvent` osztályt
* `ApplicationEventPublisher` injektálható (application context),
  majd a `publishEvent()` metódust kell meghívni
* Eseményt fogadónak az `ApplicationListener` interfészt kell implementálni

---

## Esemény

```java
public class EmployeeHasCreatedEvent extends ApplicationEvent {

    private String name;

    public EmployeeHasCreatedEvent(Object source, String name) {
        super(source);
        this.name = name;
    }

    public String getName() {
      return this.name;
    }
}
```

---

## Esemény küldése

```java
private ApplicationEventPublisher publisher;

public void saveEmployee(String name) {
    // ...
    EmployeeHasCreatedEvent event = new EmployeeHasCreatedEvent(this, name);
    publisher.publishEvent(event);
}
```

---

## Esemény fogadása

```java
@Component
public class Listener implements ApplicationListener<EmployeeHasCreatedEvent> {
    public void onApplicationEvent(EmployeeHasCreatedEvent event) {
        String name = event.getName();

        System.out.println("Employee has created [" + name + "]");
    }
}
```

---

## Standard események

* `ContextRefreshedEvent`:  `ApplicationContext` indulásakor vagy frissítésekor
* `ContextStartedEvent`: `ApplicationContext` indulásakor
* `ContextStoppedEvent`: `ApplicationContext` leállásakor
* `ContextClosedEvent`: `ApplicationContext` lezárásakor
* `RequestHandledEvent`: HTTP kérésenként

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Konfigurációs állományok

---

## Konfigurációs állományok használata

* `PropertySource` implementációk tartalmaznak konfigurációs értékeket (kulcs - érték párok használata)
* Ezekből több is lehet egy hierarchiában
* Alapból regisztrált: `SystemEnvironmentPropertySource` - környezeti változókat olvassa fel
* Betöltése pl. properties állományból `@PropertySource` annotáció
  használatával
* `Environment` tipusú példánytól kérhetőek el
* `Environment` injektálható

---

## Konfigurációs állományok használatára példa

```java
@Configuration
@PropertySource("classpath:app.properties")
public class AppConfig {

    @Autowired
    private Environment env;

    @Bean
    public EmployeeService employeeService() {
        int minLength = Integer.parseInt(env.getProperty("employeeService.minLength"));
        EmployeeService employeeService = new EmployeeService(employeeDao(), minLength);
        return employeeService;
     }
 }
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Profile

---

## Profile

* Különböző környezetben különböző beanek példányosodnak
* Névvel azonosított
* Egyszerre több profile is aktiválható
* Létezik egy `default` profile

---

## Beanek definiálása

```java
@Configuration
public class AppConfig {

    @Bean
    @Profile("normal")
    public EmployeeDao normalEmployeeDao() {
        return new SimpleEmployeeDao();
    }

    @Bean
    @Profile("postfix")
    public EmployeeDao postfixEmployeeDao() {
        return new PostfixEmployeeDao();
    }
}
```

---

## Profile aktiválása

* Több profile esetén vesszővel elválasztva
* `spring.profiles.active` property környezeti változóként
* system property-ként

```
java -Dspring.profiles.active="normal"
```

* Servlet context paraméterként
* JNDI-ben
* Integrációs tesztnél `@ActiveProfiles` <br /> annotációval

---

## Profile aktiválása kódból

* `ConfigurableEnvironment` interfészen keresztül, injektálható
* `ConfigurableEnvironment.addActiveProfile(String profile)`
* `ConfigurableEnvironment.setActiveProfiles(String... profiles)`


---

## Profile lekérdezése

```java
Environment.getActiveProfiles()
```

---

## Profile alternatívák

* Ritkán használjuk, vagy inkább soha
* `PropertySource` használatával
* Bean felüldefiniálás - WARNING
* Különböző `@Configuration` annotációval ellátott osztályok
* `@Autowired(required = false)`
* Conditional beans

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Conditional beans

---

## Conditional beans

* Egy beant akkor akarunk konfigurálni, ha valamilyen feltétetel teljesül, pl. valamilyen környezetben,   valamilyen környezeti változó esetén, vagy valami van a classpath-on
* A profile is ezen mechanizmuson alapul (`ProfileCondition`), a `@Profile` annotáció ezzel van annotálva

---

## Conditional beans példa

```java
@Bean
@Conditional(TomcatCondition.class)
public ContainerBean containerBean() {
    return new TomcatBean();
}

@Bean
@Conditional(WebsphereCondition.class)
public ContainerBean containerBean() {
    return new WebSphereBean();
}
```

---

## Condition interfész

```java
public interface Condition {
    boolean matches(ConditionContext ctxt,
        AnnotatedTypeMetadata metadata);
}
```

---

## Condition interfész implementációk

```java
public class TomcatCondition implements Condition {
    public boolean matches(
            ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment env = context.getEnvironment();
        return env.containsProperty("catalina.home");
    }
}
```
```java
public class WebSphereCondition implements Condition {
    public boolean matches(
            ConditionContext context, AnnotatedTypeMetadata metadata) {
        return context.getClassLoader().getClass()
            .getCanonicalName().startsWith("com.ibm.w");
    }
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Naplózás

---

## Naplózó keretrendszerek

* Apache Commons Logging API
* Log4j
* Log4j 2
* Logback
* `java.util.logging` - JUL
* SLF4J

---

## Spring naplózása

* Spring keretrendszerben Apache Commons Logging API függőség
* Spring 5-ben változott
* `spring-jcl` modul tartalmaz egy `org.apache.commons.logging.LogFactory` implementációt, mely Log4j 2-t vagy SLF4J-t használ, ha van a classpath-on, ellenkező esetben a JUL-t

---

## Saját naplózás

* SLF4J

```xml
<dependencies>
        <dependency>
                   <groupId>ch.qos.logback</groupId>
                   <artifactId>logback-classic</artifactId>
                   <version>1.2.3</version>
        </dependency>
</dependencies>
```

```java
private final Logger logger = LoggerFactory.getLogger(getClass());
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Aspektusorientált programozás

---

## Aspektusorientált programozás

* Az alkalmazás több pontján megjelenő funkcionalitás (un. cross-cutting concern)
* Tipikus példák: biztonság, tranzakciókezelés, naplózás

---

## AOP terminológia

* Advice: maga a funkcionalitás, amit el kell végezni
* Join point: az alkalmazás azon pontjai, ahol az advice-t be lehet illeszteni
* Pointcut: azon kiválasztott join pointok, ahol az adott advice-t le kell futtatni
* Aspect: advice és pointcut összessége

---

## Proxy

* Objektum, melyet a Spring létrehoz, miután az eredeti objektumra alkalmazta az aspektust
* Folyamat, mely során létrejön a proxy: weaving
* Spring esetén futásidejű
* Java SE dynamic proxy, ha a Spring bean interfészt implementál
* CGLIB, ha nem implementál interfészt - leszármaztatás (nem lehet `final`)

---

## AspectJ

* Aspektusorientált programozást lehetővé tevő keretrendszer
* Spring felhasznál belőle bizonyos részeket
* A Spring AOP pehelysúlyúbb megoldás

---

## Advice

* Csak metódus
* Before: metódus előtt
* After: metódus után, visszatéréstől függetlenül
* After-returning: sikeres visszatérés esetén
* After-throwing: kivétel esetén
* Around - metódust beburkolja

---

## Join point

* AspectJ kifejezésekkel
* Saját leíró nyelvvel

```java
execution(* spring.di.EmployeeService.saveEmployee(..))
```

---

## AOP függőség

```xml
<dependency>
    <groupId>org.aspectj</groupId>
    <artifactId>aspectjweaver</artifactId>
    <version>1.8.10</version>
</dependency>
```

---

## Konfiguráció annotációkkal

* `@Aspect` - aspektus definiálása
*  `@Before`, `@After`, `@AfterReturning`, `@AfterThrowing`, `@Around`

---

## AOP példa

```java
@Aspect
public class CounterAspect {

	private AtomicInteger count = new AtomicInteger();

	@Before("execution(* spring.di.EmployeeService.saveEmployee(..))")
	public void inc() {
		count.incrementAndGet();
	}

	public int getCount() {
		return count.get();
	}
}
```

---

## Pointcut újrafelhasználása

```java
@Aspect
public class CounterAspect {

	private AtomicInteger count = new AtomicInteger();

	@Pointcut("execution(* spring.di.EmployeeService.saveEmployee(..))")
	public void saveEmployee() {
	}

	@Before("saveEmployee()")
	public void inc() {
		count.incrementAndGet();
	}

	public int getCount() {
		return count.get();
	}
}
```

---

## Around aspect

```java
@Around("saveEmployee()")
public Object logMethodCall(ProceedingJoinPoint joinpoint) {
    try {
        logger.info("The method " + joinpoint.getSignature().getName() + "() begins");

        Object result = joinpoint.proceed();

        log.info("The method " + method.getName() + "() ends with " + result);
        return result;
    } catch (Throwable t) {
      log.info("The method " + method.getName() + "() ends with exception");
        throw t;
    }
}
```

---

## Around aspect tulajdonságai

* Hívás megakadályozható
* Paraméterek módosíthatóak (`ProceedingJoinPoint.getArgs()`, `ProceedingJoinPoint.proceed(Object[] objects)`)
* Visszatérési érték módosítható

---

## Aspektusok sorrendje

* Az `@Order` annotációval megadható, előbb az alacsonyabb értékű
