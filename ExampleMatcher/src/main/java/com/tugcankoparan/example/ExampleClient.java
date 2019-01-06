package com.tugcankoparan.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ExampleClient {
    @Autowired
    private EmployeeRepository exampleRepo;
    @Autowired
    private PersonRepository personRepo;

    public void run() {
        List<Employee> employees = createEmployees();
        exampleRepo.saveAll(employees);
        findAllEmployees();

        List<Person> persons = createPersons();
        personRepo.saveAll(persons);
        findAllPersons();

        findEmployeesByNameOrDept();
        findEmployeesByNameAndDept();
        findPersonByNameIgnoringIdPath();
        findEmployeeByNameAndDeptCaseInsensitive();
        findEmployeeByNameAndDeptIgnoringPathForCase();
        findEmployeeByNameEnding();
        findEmployeeByNameEndingAndByDept();
        //regex is not yet supported
        //findEmployeeByNameUsingRegex();
        findEmployeeByTransformingName();
        findPersonWithNullName();
        findEmployeeByNameIgnoringCaseAndContains();
    }

    private void findEmployeesByNameOrDept() {
        System.out.println("-- finding employees with name Diana or dept IT --");
        Employee employee = new Employee();
        employee.setName("Diana");
        employee.setDept("IT");
        System.out.println("Example entity: "+employee);
        Example<Employee> employeeExample = Example.of(employee, ExampleMatcher.matchingAny());
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findEmployeesByNameAndDept() {
        System.out.println("-- finding employees with name Diana and dept IT --");
        Employee employee = new Employee();
        employee.setName("Diana");
        employee.setDept("IT");
        System.out.println("Example entity: "+employee);
        Example<Employee> employeeExample = Example.of(employee, ExampleMatcher.matchingAll());
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findPersonByNameIgnoringIdPath() {
        System.out.println("-- finding person by name Tara ignoring id=0 --");
        Person person = new Person();
        person.setName("Tara");
        System.out.println("Example entity: "+person);
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                                                      .withIgnorePaths("id");
        Example<Person> personExample = Example.of(person, exampleMatcher);
        personRepo.findAll(personExample).forEach(System.out::println);
    }

    private void findEmployeeByNameAndDeptCaseInsensitive() {
        System.out.println("-- finding employees with name tim and dept qa ignoring cases --");
        Employee employee = new Employee();
        employee.setName("tim");
        employee.setDept("qa");
        System.out.println("Example entity: "+employee);
        Example<Employee> employeeExample = Example.of(employee, ExampleMatcher.matchingAll().withIgnoreCase());
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findEmployeeByNameAndDeptIgnoringPathForCase() {
        System.out.println("-- finding employees with name tim (ignoring case) and dept QA (not ignoring case) --");
        Employee employee = new Employee();
        employee.setName("tim");
        employee.setDept("QA");
        System.out.println("Example entity: "+employee);
        Example<Employee> employeeExample = Example.of(employee, ExampleMatcher.matchingAll().withIgnoreCase("name"));
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findEmployeeByNameEnding() {
        System.out.println("-- finding employees with name ending k --");
        Employee employee = new Employee();
        employee.setName("k");
        System.out.println("Example entity: "+employee);
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                                               .withStringMatcher(ExampleMatcher.StringMatcher.ENDING);
        Example<Employee> employeeExample = Example.of(employee, matcher);
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findEmployeeByNameEndingAndByDept() {
        System.out.println("-- finding employees with name ending k and dept IT --");
        Employee employee = new Employee();
        employee.setName("k");
        employee.setDept("IT");
        System.out.println("Example entity: "+employee);
        /*ExampleMatcher name = ExampleMatcher.matchingAll()
                                            .withMatcher("name",
                                                    ExampleMatcher.GenericPropertyMatchers.endsWith());*/
        ExampleMatcher name =
                ExampleMatcher.matchingAll()
                              .withMatcher("name",
                                      //can be replaced with lambda
                                      new ExampleMatcher.MatcherConfigurer<ExampleMatcher.GenericPropertyMatcher>() {
                                          @Override
                                          public void configureMatcher(ExampleMatcher.GenericPropertyMatcher matcher) {
                                              matcher.endsWith();
                                          }
                                      });
        Example<Employee> employeeExample = Example.of(employee, name);
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findEmployeeByNameUsingRegex() {
        System.out.println(" -- getting all Employees with name regex D.*a.*a --");
        Employee employee = new Employee();
        employee.setName("D.*a.*a");
        System.out.println("Example entity: "+employee);
        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                                            .withStringMatcher(ExampleMatcher.StringMatcher.REGEX);
        Example<Employee> employeeExample = Example.of(employee, matcher);
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findEmployeeByTransformingName() {
        System.out.println(" -- getting all Employees with dept and transforming name during execution time  --");
        Employee employee = new Employee();
        employee.setName("Tim");
        employee.setDept("qa");
        System.out.println("Example entity: "+employee);
        ExampleMatcher matcher = ExampleMatcher.matchingAll().

                withTransformer("dept",
                        //can be replaced with lambda
                        new ExampleMatcher.PropertyValueTransformer() {
                            @Override
                            public Optional<Object> apply(Optional<Object> o) {
                                if (o.isPresent()) {
                                    return Optional.of(((String) o.get()).toUpperCase());
                                }
                                return o;
                            }
                        });
        Example<Employee> employeeExample = Example.of(employee, matcher);
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findPersonWithNullName() {
        System.out.println("-- finding person with null name --");
        Person person = new Person();
        person.setName(null);
        System.out.println("Example entity: "+person);
        ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
                                                      .withIgnorePaths("id")
                                                      .withIncludeNullValues();
        Example<Person> personExample = Example.of(person, exampleMatcher);
        personRepo.findAll(personExample).forEach(System.out::println);
    }

    private void findEmployeeByNameIgnoringCaseAndContains() {
        System.out.println("-- finding employees with ignored case name contains 'AC' --");
        Employee employee = new Employee();
        employee.setName("AC");
        employee.setDept("IT");
        System.out.println("Example entity: "+employee);
        ExampleMatcher matcher = ExampleMatcher.matching()
                                             .withIgnoreCase("name")
                                             .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Employee> employeeExample = Example.of(employee, matcher);
        Iterable<Employee> employees = exampleRepo.findAll(employeeExample);
        for (Employee e : employees) {
            System.out.println(e);
        }
    }

    private void findAllEmployees() {
        System.out.println(" -- getting all Employees --");
        Iterable<Employee> iterable = exampleRepo.findAll();
        for (Employee employee : iterable) {
            System.out.println(employee);
        }
    }

    private void findAllPersons() {
        System.out.println(" -- getting all Persons --");
        Iterable<Person> iterable = personRepo.findAll();
        for (Person person : iterable) {
            System.out.println(person);
        }
    }

    private static List<Employee> createEmployees() {
        return Arrays.asList(Employee.create("Diana", "IT"),
                Employee.create("Mike", "Admin"),
                Employee.create("Tim", "QA"),
                Employee.create("Jack", "IT"));
    }

    private static List<Person> createPersons() {
        return Arrays.asList(Person.create("Joe"),
                Person.create("Tara"),
                Person.create(null));
    }
}