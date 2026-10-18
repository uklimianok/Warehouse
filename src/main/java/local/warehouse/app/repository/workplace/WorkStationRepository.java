package local.warehouse.app.repository.workplace;

import org.springframework.data.jpa.repository.JpaRepository;

import local.warehouse.app.entity.workplace.WorkStation;

public interface WorkStationRepository extends JpaRepository<WorkStation, Long> {

    boolean existsByWorkshopId(long workshopId);
    
    boolean existsByStationNumber(String stationNumber);
}
