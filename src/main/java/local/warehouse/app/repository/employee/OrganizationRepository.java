package local.warehouse.app.repository.employee;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.employee.Organization;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    boolean existsByOrganizationTypeId(long organizationTypeId);
    
    boolean existsByOrganizationNumber(String organizationNumber);
}
