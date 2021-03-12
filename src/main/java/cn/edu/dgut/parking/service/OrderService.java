package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.Order;
import cn.edu.dgut.parking.repository.OrderRepository;
import cn.edu.dgut.util.GenerateOrderNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    //生成新订单
    public Long add(Order order){
        Thread thread = new Thread(() -> {
            GenerateOrderNum generateOrderNum = new GenerateOrderNum();
            order.setOrderNum(generateOrderNum.generate());
        });
        thread.start();
        order.setInTime(LocalDateTime.now());
        orderRepository.save(order);
        return order.getId();
    }
    public Order update(Order order){
        orderRepository.save(order);
        return order;
    }
    public Order findById(Long id){
        return orderRepository.findById(id).orElse(null);
    }
    public Order findByIdAndOrderCompleted(Long id, Boolean orderCompleted){
        return orderRepository.findByIdAndOrderCompleted(id, orderCompleted);
    }
    public Order findByOrderNum(String orderNum){
        return orderRepository.findByOrderNum(orderNum);
    }
}
