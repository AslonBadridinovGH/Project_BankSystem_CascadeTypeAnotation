package uz.pdp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.entity.Employee;


import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    boolean existsByEmail(String email);


    Optional<Employee> findByEmailCodeAndEmail(String emailCode,  String email);

    Optional<Employee> findByEmail(String email);


}
