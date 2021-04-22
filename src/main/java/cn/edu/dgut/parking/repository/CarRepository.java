package cn.edu.dgut.parking.repository;

import cn.edu.dgut.parking.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByLincesePlate(String lincesePlate);
}
