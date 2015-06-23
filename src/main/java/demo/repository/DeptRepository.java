package demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import demo.entity.Dept;

@Repository
public interface DeptRepository extends CrudRepository<Dept, Long> {

}
