package local.warehouse.app.repository.workplace;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.workplace.Workshop;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {}
