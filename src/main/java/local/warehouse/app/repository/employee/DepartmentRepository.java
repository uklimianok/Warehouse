package local.warehouse.app.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.employee.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    boolean existsByName(String name);
}
