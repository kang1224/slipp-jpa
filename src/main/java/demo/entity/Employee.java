package demo.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "dept")
@Entity
public class Employee implements Serializable {

    private static final long serialVersionUID = -7036573119898240658L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Integer age;
    private Integer salary;
    private Long deptId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deptId", insertable = false, updatable = false)
    private Dept dept;

    public Employee(String name, Integer age, Integer salary, Long deptId) {
        this.name = name;
        this.age = age;
        this.salary = salary;
        this.deptId = deptId;
    }
}
