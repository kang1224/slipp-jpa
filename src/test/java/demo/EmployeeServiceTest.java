package demo;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.extern.slf4j.Slf4j;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import demo.entity.Dept;
import demo.entity.Employee;
import demo.entity.Employee_;
import demo.repository.DeptRepository;
import demo.repository.EmployeeRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CriteriaExampleApplicationTests.class)
@WebAppConfiguration
@Slf4j
public class EmployeeServiceTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DeptRepository deptRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Before
    public void init() {
        Dept d1 = new Dept(1L, "Sales", null);
        Dept d2 = new Dept(2L, "Marketing", null);
        Dept d3 = new Dept(3L, "Accounting", null);
        Dept d4 = new Dept(4L, "Technology", null);

        deptRepository.save(d1);
        deptRepository.save(d2);
        deptRepository.save(d3);
        deptRepository.save(d4);

        employeeRepository.save(new Employee("Thomas", 20, 5000, 1L));
        employeeRepository.save(new Employee("Jason", 23, 5500, 4L));
        employeeRepository.save(new Employee("Mayla", 25, 9000, 4L));
        employeeRepository.save(new Employee("Nisha", 22, 5500, 2L));
        employeeRepository.save(new Employee("Randy", 23, 6000, 4L));
        employeeRepository.save(new Employee("Ritu", 25, null, 3L));

    }

    @Test
    public void testSelectJpa() throws Exception {

        Iterable<Dept> result = deptRepository.findAll();
        log.debug("testSelectJpa {}", result);
    }

    @Test
    public void testSelectCriteria() throws Exception {

        TypedQuery<Dept> query = entityManager.createQuery("SELECT d FROM Dept d", Dept.class);

        query.getResultList();

    }

    @Test
    public void testSelectCriteriaTypeSafe() throws Exception {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = builder.createQuery(Employee.class);

        Root<Employee> employee = query.from(Employee.class);

        //formatter:off
        query.where(builder
                     .and(
                        builder.gt(employee.get(Employee_.salary), 5000),
                        builder.le(employee.get(Employee_.salary), 9000)
                                ));
        //formatter:on

        entityManager.createQuery(query.select(employee)).getResultList();

    }

    @Test
    public void testJoinOrder() throws Exception {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = builder.createQuery(Employee.class);

        Root<Employee> employee = query.from(Employee.class);
        Join<Employee, Dept> dept = employee.join(Employee_.dept);

        //formatter:off
        query.where(builder
                     .and(
                        builder.gt(employee.get(Employee_.salary), 5000),
                        builder.le(employee.get(Employee_.salary), 9000)
                                ));
        //formatter:on

        query.orderBy(builder.asc(dept.get("id")));

        Iterable<Employee> result = entityManager.createQuery(query.select(employee)).getResultList();

        log.debug("{}", result);

    }

    @Test
    public void testComparison() throws Exception {

        Integer ageMin = 20;
        Integer ageMax = 24;
        String excludName = "Randy";

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = builder.createQuery(Employee.class);

        Root<Employee> employee = query.from(Employee.class);
        Join<Employee, Dept> dept = employee.join(Employee_.dept);

        Expression<Integer> age = employee.get(Employee_.age);
        Expression<String> name = employee.get(Employee_.name);

        Predicate pAge = builder.between(age, ageMin, ageMax);
        Predicate pName = builder.notEqual(name, excludName);

        query.where(pAge, pName);

        query.orderBy(builder.asc(dept.get("id")));

        Iterable<Employee> result = entityManager.createQuery(query.select(employee)).getResultList();

        log.debug("{}", result);

    }
}
