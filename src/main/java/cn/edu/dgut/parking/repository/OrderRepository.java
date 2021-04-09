package cn.edu.dgut.parking.repository;

import cn.edu.dgut.parking.model.Car;
import cn.edu.dgut.parking.model.Order;
import cn.edu.dgut.parking.model.User;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndOrderCompleted(Long id, Boolean orderCompleted);
    Order findByOrderNum(String orderNum);
    Set<Order> findByLicensePlate(String plate);
    Order findByLicensePlateAndOrderCompleted(String lincePlate, Boolean orderCompleted);
    Page<Order> findAllByUser(User user, Pageable pageable);
    Page<Order> findAllByCar(Car car, Pageable pageable);
    Page<Order> findAllByOrderCompleted(Boolean flag, Pageable pageable);
}
