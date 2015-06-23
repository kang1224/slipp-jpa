package demo.entity;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Employee.class)
public class Employee_ {

    public static volatile SingularAttribute<Employee, Long> id;
    public static volatile SingularAttribute<Employee, Long> salary;
    public static volatile SingularAttribute<Employee, Integer> age;
    public static volatile SingularAttribute<Employee, String> name;
    public static volatile SingularAttribute<Employee, Dept> dept;
}
