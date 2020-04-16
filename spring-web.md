class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
# Alkalmazásfejlesztés Spring keretrendszerrel - Webalkalmazás fejlesztés

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Tematika

---

## Tematika 1.

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Bevezetés a Spring MVC használatába

---

## Bevezetés a Spring MVC használatába

* Spring Framework modul
* MVC architektúra
  * Model: megjelenítendő adat
  * View: adat renderelése felhasználó számára megjeleníthető formára
  * Controller: adatkérés a felhasználótól, view váltás
* Web alkalmazások webes felületére, RESTful <br /> webszolgáltatások implementálására
* View réteg cserélhető: JSP, Thymeleaf, stb.

---

## Feldolgozás folyamata

* Kérés-válasz kommunikáció
* `DispatcherServlet` fogadja a kérést (Front controller Java EE tervezési minta)
* *Handler mapping* választja ki a controllert
* *Controller* hívja az üzleti logikát, és visszaadja a view logikai nevét és a modelt
* *View resolver* kiválasztja a view logikai neve alapján a view-t
* *View* lerendereli az oldalt a model felhasználásával

---

## Feldolgozás RESTful webszolgáltatások esetén

* Controller csak modelt ad vissza
* A model azonnal renderelésre kerül pl. JSON formában

---

## Maven pom.xml

```xml
<packaging>war</packaging>
```

```xml
<dependency>
	<groupId>org.springframework</groupId>
	<artifactId>spring-webmvc</artifactId>
</dependency>

<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
    <scope>provided</scope>
</dependency>
```

---

## Konténer függőség

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.eclipse.jetty</groupId>
            <artifactId>jetty-maven-plugin</artifactId>
            <version>9.4.14.v20181114</version>
        </plugin>
    </plugins>
</build>
```

---

## WebApplicationContext

* Hierarchiában állhat
* `ServletContext`-ben tárolva
* Szülő context: un. root context, repository és service réteg
* Gyermek context: un. web context, controller réteg
* Gyermek látja a szülőben lévő beaneket, de visszafele nem

---

## Konfiguráció web.xml nélkül

```java
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

}
```

---

## Servlet API háttér

* `WebApplicationInitializer` interfészt implementálja
* A `SpringServletContainerInitializer` tölti be
* Implementálj a `javax.servlet.ServletContainerInitializer` interfészt

---

## Configuration osztály

```java
@Configuration
@EnableWebMvc
@ComponentScan("spring.controller")
public class WebConfig {
}
```

---

## Controller

```java
@Controller
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello SpringMVC";
    }
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Thymeleaf view

---

## Thymeleaf függőség

```xml
<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf</artifactId>
    <version>3.0.11.RELEASE</version>
</dependency>

<dependency>
    <groupId>org.thymeleaf</groupId>
    <artifactId>thymeleaf-spring5</artifactId>
    <version>3.0.11.RELEASE</version>
</dependency>
```

---

## ViewResolver

```java
@Bean
public ViewResolver viewResolver() {
    ThymeleafViewResolver resolver = new ThymeleafViewResolver();
    resolver.setTemplateEngine(templateEngine());
    resolver.setCharacterEncoding("UTF-8");
    return resolver;
}

@Bean
public SpringTemplateEngine templateEngine() {
    SpringTemplateEngine engine = new SpringTemplateEngine();
    engine.setTemplateResolver(templateResolver());
    return engine;
}
```

---

## TemplateResolver

```java
@Bean
public ITemplateResolver templateResolver() {
    SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
    resolver.setApplicationContext(applicationContext);
    resolver.setPrefix("/WEB-INF/templates/");
    resolver.setSuffix(".html");
    resolver.setTemplateMode("HTML5");
    return resolver;
}
```

---

## Controller

```java
@Controller
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @RequestMapping("/")
    public ModelAndView listEmployees() {
      return new ModelAndView("index",
                  "employees", employeeService.listEmployees());
    }
}
```

---

## Template

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<body>

  <table>

    <!-- Table head -->

    <tbody>
      <th:block th:each="employee : ${employees}">
          <tr>
              <td th:text="${employee.id}"></td>
              <td th:text="${employee.name}"></td>
          </tr>
      </th:block>
    </tbody>
  </table>

</body>
</html>
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Erőforrások kezelése

---

## Erőforrások

```java
@Configuration
@EnableWebMvc
@ComponentScan("spring.controller")
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
      registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
  }

}
```

---

## Állományok

* Webalkalmazás gyökerében
* Maven esetén az `/src/main/webapp/resources/` könyvtárban
* Javasolt alkönyvtárak használata, pl. `css`, `js`, `images`, stb.

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Thymeleaf oldalstruktúra

---

## Oldaltöredékek

* Oldaltöredékek deklarálása pl. a `fragments` alkönyvtárba
* Oldaltöredék deklarálása a `th:fragment` attribútummal
* Oldaltöredék felhasználása a `th:insert` vagy `th:replace` attribútummal

---

## Oldaltöredék

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:fragment="header">
    <title>Employees</title>
    <link rel="stylesheet" type="text/css" th:href="@{/resources/css/bootstrap.min.css}">
</head>

<body>

<div class="container">

    <h1 th:fragment="title">Employees</h1>

</div>

</body>
</html>
```

---

## Oldaltöredék használata

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="fragments/page :: header">
</head>

<body>

<div class="container">

    <h1 th:replace="fragments/page :: title">Employees</h1>

</div>
</body>
</html>
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Controllerek használata

---

## Controllerek

* POJO
* Annotációk erős használata
* Nem feltétlenül van Servlet API függősége
* Metódusok neve, paraméterezése flexibilis

---

## @RequestMapping annotáció

* Osztályszintű és/vagy metódus szintű
* Megadható a HTTP metódus a `method` paraméterrel
* Ant-szerű megadási mód (pl. `/myPath/*.html`)

---

## URL paraméterek kezelése

* `@RequestParam` annotációval
* Kötelező, kivéve a `required = "false"` attribútum megadásakor
* Automatikus típuskonverzió


```java
 @RequestMapping(value = "/employees", method = RequestMethod.GET)
 public String findEmployeeById(@RequestParam("id") int id) {
    // ...
 }
```

---

## URL részletek kezelése

```java
@RequestMapping("/employees/{id}")
public ModelAndView findEmployeeById(@PathVariable("id") long id) {
    // ...
}
```

---

## Sütik kezelése a @CookieValue annotációval

```java
@RequestMapping("/displayRequestInfo.html")
public void displayHeaderInfo(@CookieValue("JSESSIONID") String cookie) {
    //...
}
```

---

## Fejlécek kezelése @RequestHeader annotációval

```java
@RequestMapping("/displayRequestInfo.html")
public void displayHeaderInfo(@RequestHeader("Accept-Encoding") String encoding,
        @RequestHeader("Keep-Alive") long keepAlive) {
    //...
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Hozzáadás és szerkesztés (@ModelAttribute használata)

---

## Űrlap

* *Post/Redirect/Get* működés
* Űrlap megjelenítés: GET
  * Űrlap előkészítése, modell feltöltése
  * Űrlap megjelenítése
* Űrlap kitöltés és beküldés: POST
  * `@ModelAttribute`
* Átirányítás
  * Flash attribútum: HTTP sessionbe, <br /> következő oldal eltávolítja
  * Üzenet megjelenítése

---

## @ModelAttribute

* Bármennyi metódus lehet, melyet el lehet látni `@ModelAttribute` annotációval
* Visszatérési értéke bekerül a modellbe
* Tipikusan pl. űrlap feltöltéshez, legördülő listák feltöltésére
* Neve a típusból képződik, `name` attribútummal személyre szabható
* Ha `@RequestMapping` metódusra kerül, <br /> a visszatérés nem view név, hanem modell attribútum
* Opcionálisan: `@RequestMapping` annotációval <br /> ellátott controller metódusban töltjük fel a modellt

---

## Űrlap megjelenítés - controller

```java
@ModelAttribute
public Employee employee() {
    return new Employee();
}

@RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView listEmployees() {
  return new ModelAndView("index", "employees", employeeService.listEmployees());
}
```

---

## Űrlap megjelenítés - view

```html
<form th:action="@{/}" th:object="${employee}" action="/" method="post">
  <input th:field="*{name}" />
  <button type="submit">Create employee</button>
</form>
```

---

## Speciális view nevek

* `redirect:` prefix
  *  `HttpServletResponse.sendRedirect()` hívással egyenértékű
* `forward:` prefix
  * `RequestDispatcher.forward()` hívással egyenértékű

---

## Űrlap feldolgozás

* `@ModelAttribute` annotációval ellátott paraméter
* Service réteg hívása
* Flash attribútum beállítása
  * `RedirectAttributes` paraméter
* Átirányítás

---

## Űrlap feldolgozás - controller

```java
@RequestMapping(value = "/", method = RequestMethod.POST)
public String saveEmployee(@ModelAttribute Employee employee,
        RedirectAttributes redirectAttributes) {
    employeeService.saveEmployee(employee.getName());
    redirectAttributes.addFlashAttribute("message", "Employee has saved");
    return "redirect:/";
}
```

---

## Üzenet megjelenítése

```html
<div th:if="${message}" th:text="${message}">Message</div>
```

---

## CharacterEncodingFilter

```java
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        return new Filter[] {characterEncodingFilter};
    }
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Tesztelés

---

## Unit tesztelés

```java
@RunWith(MockitoJUnitRunner.class)
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Test
    public void testListEmployees() {
        when(employeeService.listEmployees()).thenReturn(List.of(new Employee("John Doe")));

        ModelAndView modelAndView = employeeController
          .listEmployees();
        assertEquals("index", modelAndView.getViewName());
        assertEquals(List.of("John Doe"),
            ((List<Employee>) modelAndView.getModel()
              .get("employees")).stream().map(Employee::getName)
              .collect(Collectors.toList()));
    }
}
```

---

## Integrációs tesztelés

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextHierarchy({@ContextConfiguration(classes = BackendConfig.class),
        @ContextConfiguration(classes = WebConfig.class)})
@WebAppConfiguration
public class EmployeeTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext).build();
    }
}
```

---

## Teszt metódus

```java
@Test
public void testListEmployees() throws Exception {
    mockMvc.perform(post("/")
            .param("name", "John Doe"));


    mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attribute("employees",
                    hasItem(hasProperty("name", equalTo("John Doe")))))
            .andExpect(content().string(containsString("John Doe")));
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Handlerek

---

## Handlerek

* Futtatás a HTTP kérés beérkezésekor, HTTP válasz kiküldése előtt
* Servlet filter, Springes szolgáltatásokkal
* Webalkalmazás AOP-ja

---

## HandlerInterceptor implementation

```java
@Component
public class TimeBasedAccessInterceptor extends HandlerInterceptorAdapter {

    private int openingTime = 9;

    private int closingTime = 17;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        int hour = LocalTime.now().getHour();
        if (openingTime <= hour && hour < closingTime) {
            return true;
        }
        response.sendRedirect("/outsideOfficeHours.html");
        return false;
    }
}
```

---

## HandlerInterceptor konfigurálása

```java
@Configuration
@EnableWebMvc
@ComponentScan("spring.controller")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TimeBasedAccessInterceptor timeBasedAccessInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeBasedAccessInterceptor);
    }

    // ...

}
```


---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## I18N

---

## MessageSource

* Nyelvi üzenetek, kulcs érték párok
* Java SE ResourceBundle-ön alapulhat: `ResourceBundleMessageSource`
* Properties állományokban (figyeljünk a karakterkódolásra)
* Feloldás Locale alapján

```java
@Bean
public MessageSource messageSource() {
    ResourceBundleMessageSource resourceBundleMessageSource =
            new ResourceBundleMessageSource();
    resourceBundleMessageSource.setBasename("messages");
    resourceBundleMessageSource.setDefaultEncoding("utf-8");
    return resourceBundleMessageSource;
}
```
---

## Feloldás

* Base name: `messages`
* Locale: `en_US`
* Keresés:
  * `messages_en_US.properties`
  * `messages_en.properties`
  * `messages_hu_HU.properties` <br /> - platform default locale
  * `messages_hu.properties`
  * `messages.properties`

---

## Thymeleaf

```html
<h1 th:text="#{title}">Employees</h1>

<h1 th:text="#{welcome.message(${employee.name})}">Hello Joe!</h1>
```

```
title = Employees
welcome.message = Hello {0}!
```

---

## Controller

* `Locale` `@RequestMapping` metódus paramétereként használható
* `MessageSource` injektálható

```java
@Controller
public class EmployeesController {

    private EmployeeService employeeService;

    private MessageSource messageSource;

    // ...

    public EmployeesController(EmployeeService employeeService,
            MessageSource messageSource) {
        this.employeeService = employeeService;
        this.messageSource = messageSource;
    }

}
```

---

## Controller metódus

```java
public String saveEmployee(@ModelAttribute Employee employee RedirectAttributes redirectAttributes,
                               Locale locale) {
    employeeService.saveEmployee(employee.getName(), employee.getDateOfBirth());
    String message = messageSource.getMessage("employee.saved.success",
            new Object[]{employee.getName()}, locale);
    redirectAttributes.addFlashAttribute("message", message);
    return "redirect:/";
}
```

---

## I18N

* Locale objektumon alapszik
* Locale feloldás: `LocaleResolver`
  * `AcceptHeaderLocaleResolver`:  inspects the `accept-language header`
  * `CookieLocaleResolver`
  * `SessionLocaleResolver`
* Locale váltás: `LocaleChangeInterceptor`

---

## LocaleResolver és LocaleChangeInterceptor deklarálása

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(localeChangeInterceptor());
}

@Bean
public LocaleResolver localeResolver() {
    return new CookieLocaleResolver();
}

@Bean
public LocaleChangeInterceptor localeChangeInterceptor(){
    LocaleChangeInterceptor localeChangeInterceptor
        = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang");
    return localeChangeInterceptor;
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Validáció

---

## Validáció bevezetése

* Paraméteren `@ModelAttribute` annotáció helyett `@Valid` annotáció
* `BindingResult` típusú plusz paraméter

---

## JSR-303/JSR-349

```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.4.Final</version>
</dependency>
```

```java
public class Employee {

    // ...

    @NotEmpty
    private String name;

}
```

---

## Controller

```java
@RequestMapping(value = "/", method = RequestMethod.POST)
public String saveEmployee(@Valid Employee employee, BindingResult bindingResult,
        RedirectAttributes redirectAttributes) {
    if (bindingResult.hasErrors()) {
        return "index";
    }
    employeeService.saveEmployee(employee.getName(), employee.getDateOfBirth());
    redirectAttributes.addFlashAttribute("message", "Employee has saved");
    return "redirect:/";
}
```

---

## HTML template

```html
<div class="form-group" id="name-input"
        th:classappend="${#fields.hasErrors('name')} ? ' is-invalid'">
    <label for="name-input">Name</label>
    <input th:field="*{name}" class="form-control"
        th:classappend="${#fields.hasErrors('name')} ? ' is-invalid'"/>
    <div th:if="${#fields.hasErrors('name')}"
          th:errors="*{name}"
          class="invalid-feedback" />
</div>
```

---

## Személyre szabás

* `NotEmpty.employee.name`
* `NotEmpty.name`
* `NotEmpty.java.lang.String`
* `NotEmpty`

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Fájlkezelés

---

## Fájlletöltés

```java
@RequestMapping(value = "/images/{name:.+}", method = RequestMethod.GET)
@ResponseBody
public Resource getImage(@PathVariable("name") String name) {
    return new ByteArrayResource(imageService.loadFile(name));
}
```

* `Resource` implementációk: pl. `ByteArrayResource`, `ClassPathResource`, `FileSystemResource`, `InputStreamResource`

---

## Fájlfeltöltés

* Commons FileUpload vagy Servlet 3.0
* Különböző *MultipartResolver* implementációk

---

## Servlet 3.0

```java
public class WebAppInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        MultipartConfigElement multipartConfigElement =
                new MultipartConfigElement(System.getProperty("java.io.tmpdir"),
                        5 * 1024 * 1025,
                        20 * 1024 * 1024,
                        0);
        registration.setMultipartConfig(multipartConfigElement);
    }
}
```

---

## StandardServletMultipartResolver

```java
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}
```

---

## Űrlap

```html
<form th:action="@{/upload-image}" th:object="${uploadImageForm}"
        method="post" enctype="multipart/form-data">

    <!-- ... -->

    <div class="form-group" id="file-input">
        <label for="file-input">File</label>
        <input type="file" th:field="*{file}" class="form-control-file"/>
    </div>

    <button type="submit" class="btn btn-primary">Upload file</button>

</form>
```

---

## Modell osztály

```java
public class UploadImageForm {

    private String name;

    private MultipartFile file;

    // getter és setter metódusok

}
```

---

## Controller

```java
@RequestMapping(value = "/upload-image", method = RequestMethod.POST)
public String uploadImage(@ModelAttribute UploadImageForm uploadImageForm) {
    try {
        imageService.saveImage(uploadImageForm.getName(), uploadImageForm.getFile().getBytes());
    }
    catch (IOException ioe) {
        throw new IllegalStateException("Cannot upload file", ioe);
    }
    // Üzenetkezelés
    return "redirect:/";
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Témák használata

---

## Téma

* Statikus erőforrások gyűjteménye, pl. css állományok és képek, melyek a megjelenésért felelősek

```java
@Bean
public ThemeSource themeSource() {
    ResourceBundleThemeSource resourceBundleThemeSource =
            new ResourceBundleThemeSource();
    resourceBundleThemeSource.setBasenamePrefix("theme-");
    return resourceBundleThemeSource;
}
```

---

## Téma properties állományok

```properties
# theme-normal.properties tartalma

css = /resources/css/normal.min.css
```

```properties
# theme-grayscale.properties tartalma

css = /resources/css/grayscale.min.css
```

---

## ThemeResolver

Pl. `FixedThemeResolver`, `SessionThemeResolver`, `CookieThemeResolver`

```java
@Bean
public ThemeResolver themeResolver() {
    SessionThemeResolver sessionThemeResolver = new SessionThemeResolver();
    sessionThemeResolver.setDefaultThemeName("normal");
    return sessionThemeResolver;
}
```

---

## Thymeleaf támogatás

```html
<link rel="stylesheet" type="text/css" th:href="@{${#themes.code('css')}}">
```

---

## ThemeChangeInterceptor

```java
@Override
public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(themeChangeInterceptor());
}

@Bean
public ThemeChangeInterceptor themeChangeInterceptor() {
    ThemeChangeInterceptor themeChangeInterceptor = new ThemeChangeInterceptor();
    themeChangeInterceptor.setParamName("theme");
    return themeChangeInterceptor;
}
```

---

## Téma váltás

```html
<div><a href="?theme=normal">Normal</a> <a href="?theme=grayscale">Grayscale</a></div>
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Hiba és kivételkezelés

---

## Hiba és kivételkezelés

* Servlet szabvány szerint
* Spring MVC
  * `SimpleMappingExceptionResolver` - megfeleltetés a kivétel neve és view név között
  * `DefaultHandlerExceptionResolver` - Spring MVC belső kivételeit HTTP státusz kódra fordítja

---

## Kivételkezelés folytatás

  * `ResponseStatusExceptionResolver` - `@ResponseStatus` annotációval ellátott kivételeket fordítja át
  * `ExceptionHandlerExceptionResolver` - `@ExceptionHandler` annotációval ellátott metódusok hívását végzi

---

## Servlet hibakezelés

* Nincs rá Java konfiguráció a Servlet 3 szabványban

```xml
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
    http://java.sun.com/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">

    <error-page>
        <error-code>404</error-code>
        <location>/error</location>
    </error-page>

    <error-page>
        <error-code>500</error-code>
        <location>/error</location>
    </error-page>

</web-app>
```

---

## Thymeleaf template

* Request attribute-ként elérhető a státuszkód, üzenet, kivétel típusa

```html
<div th:text="${#request.getAttribute('javax.servlet.error.status_code')}"></div>
<div th:text="${#request.getAttribute('javax.servlet.error.message')}"></div>
<div th:text="${#request.getAttribute('javax.servlet.error.exception_type')}"></div>
```

---

## HTTP státuszkód beállítása

```java
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Employee not found")
public class EmployeeNotFoundException extends RuntimeException {
}
```

---

## ExceptionResolver

```java
@Bean
public SimpleMappingExceptionResolver exceptionResolver() {
    SimpleMappingExceptionResolver simpleMappingExceptionResolver =
            new SimpleMappingExceptionResolver();

    simpleMappingExceptionResolver.setDefaultErrorView("defaulterror");
    return simpleMappingExceptionResolver;
}
```

---

## Exception mapping

```java
Properties mapping = new Properties();
mapping.put("EmployeeNotFoundException", "employeenotfound");
simpleMappingExceptionResolver.setExceptionMappings(mapping);
```

---

## Thymeleaf

```html
<div th:text="${exception.message}"></div>
```

---

## ExceptionHandler a controlleren

```java
@ExceptionHandler(EmployeeNotFoundException.class)
@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Employee not found")
public void handleException(EmployeeNotFoundException ex) {    
}
```

```java
@ExceptionHandler(EmployeeNotFoundException.class)
public String handleException(EmployeeNotFoundException ex) {
    return "employeeerror";
}
```

```java
@ExceptionHandler(EmployeeNotFoundException.class)
public ModelAndView handleException(EmployeeNotFoundException ex) {
    return new ModelAndView("employeeerror", Map.of(),
        HttpStatus.NOT_FOUND);
}
```

---

## ControllerAdvice

```java
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ModelAndView handleException(EmployeeNotFoundException ex) {
        return new ModelAndView("employeeerror", Map.of(), HttpStatus.NOT_FOUND);
    }
}
```

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## Backend integráció

---

## Integráció lépései - konfiguráció

* Függőségek: `spring-orm`, `spring-data-jpa`, `hibernate-entitymanager`, `jaxb-api`, `flyway-core`
* Konfiguráció: `@EnableJpaRepositories`, `@PropertySource`, `DataSource`, `Flyway`, `JpaTransactionManager`, `JpaVendorAdapter`, `LocalContainerEntityManagerFactoryBean`,
* `application.properties`

---

## Integráció lépései - funkcionalitás

* `db.migration` könyvtár tartalma
* Entitások, `packagesToScan`
* Repository-k
* Service réteg
* Tesztesetek

---

class: inverse, center, middle

.training360-logo[![Training360](resources/training360-logo.svg)]
Lecke
## RESTful webszolgáltatások

---

## RESTful webszolgáltatások tulajdonságai

* Roy Fielding: Architectural Styles and the Design of Network-based Software Architectures, 2000
* Representational state transfer
* Alkalmazás erőforrások gyűjteménye, melyeken CRUD műveleteket lehet végezni
* HTTP protokoll erőteljes használata
* JSON formátum használata
* Egyszerűség, skálázhatóság, platformfüggetlenség

---

## HTTP-ből újrafelhasznált tulajdonságok

* Kliens-szerver, kérés-válasz, állapotmentes
* URI
* HTTP metódusok
* Hibakezelés, státuszkódok
* Header
* Content negotiation, mime-type
* Cache

---

## RESTful webszolgáltatások Spring MVC használatával

* Standard controllerek
* `@RequestMapping` annotation, `@GetMapping`,
* `@PathVariable` használata
* `@RequestBody` annotáció
* `@ResponseBody`, helyette `@RestController`

---

## JSON Mapping View

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.7</version>
</dependency>
```

---

## Integrációs tesztelés


```xml
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.4.0</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path-assert</artifactId>
    <version>2.4.0</version>
    <scope>test</scope>
</dependency>
```

---

## Integrációs teszt metódus

```java
@Test
public void testListEmployees() throws Exception {
    mockMvc.perform(post("/api/employees")
            .header("Content-Type", "application/json")
            .content(new ObjectMapper().writeValueAsString(new Employee("John Doe"))));


    mockMvc.perform(get("/api/employees"))
            .andExpect(status().isOk())
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
            .andExpect(jsonPath("$[0].name", is("John Doe")));
}
```
