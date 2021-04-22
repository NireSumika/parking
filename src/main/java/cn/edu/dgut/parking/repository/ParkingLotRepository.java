package cn.edu.dgut.parking.repository;

import cn.edu.dgut.parking.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, Long> {
    ParkingLot findByParkingLotName(String parkingLotName);
}
