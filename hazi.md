# Házi feladat

Ajánlott a jelenlegi projektet egy `spring-homework` könyvtárba átmásolni,
majd azt módosítani. (Megoldást megtalálod ebben a könyvtárban.)

Módosítsd a jelenlegi projektet:

* Az XML-es konfig és teszteset törölhető.
* Legyen egy `Employee` osztály, egy `Long id` és egy `String name`
attribútuma! Használd a `@Data` `@AllArgsConstructor` Lombok annotációt!
* Az `EmployeeDao` osztályban implementáld a következő metódusokat:

```java
public Employee saveEmployee(String name) {}

public List<Employee> getEmployees() {}

public Employee findEmployeeById(long id) {}

public Employee updateEmployee(long id, String name) {}

public void deleteEmployee(long id) {}

public void emptyEmployees() {}
```

Tárold az alkalmazottakat `List<Employee>` adatstruktúrában! Használj streameket!
Az id-t egy `AtomicLong` példányból oszd ki, mindig a következőt!

* Az `EmployeeService`-ben valósítsd meg ugyanezeket a metódusokat, de mindegyik
metódus a dao-hoz delegálja a kérést! Figyelj, az `updateEmployee` metódus is
a property alapján nagybetűsítsen (, és ne legyen kódismétlés)!
* A `System.out.println` hívásokat mindenütt cseréld SLF4J-re!
* Egészítsd ki a `EmployeeServiceIT` osztályt, minden metódusra írj egy
külön tesztesetet!
* Ellenőrizd a megoldásod a megadott megoldás alapján!
