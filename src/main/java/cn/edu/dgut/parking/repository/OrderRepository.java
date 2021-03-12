package cn.edu.dgut.parking.repository;

import cn.edu.dgut.parking.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByIdAndOrderCompleted(Long id, Boolean orderCompleted);
    Order findByOrderNum(String orderNum);
}
