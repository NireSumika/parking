package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.Order;
import cn.edu.dgut.parking.model.User;
import cn.edu.dgut.parking.service.OrderService;
import cn.edu.dgut.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RequestMapping("/order")
@RestController

public class OrderController {
    @Autowired
    private OrderService orderService;
    private UserService userService;
//    @GetMapping("/")
//    public Order order(@RequestParam("id") Long id){
//        return orderService.findById(id);
//    }

    @PostMapping("/add")
    public Long add(@RequestBody Order order){
        //TODO 校验
        Order newOrder = new Order();
        newOrder.setInGate(order.getInGate());
        newOrder.setParkingLotName(order.getParkingLotName());
        newOrder.setLicensePlate(order.getLicensePlate());
        newOrder.setInPassWay(order.getInPassWay());
        return orderService.add(newOrder);
    }
    @PostMapping("/update_orderSubmissionTime")
    public Order update_orderSubmissionTime(@RequestBody Order order){
        //TODO 校验
        Order order_original = orderService.findByIdAndOrderCompleted(order.getId(), false);
        order_original.setOrderSubmissionTime(LocalDateTime.now());
        Duration parkingTime = Duration.between(order_original.getInTime(), order_original.getOrderSubmissionTime().plusHours(3).plusSeconds(365));
        order_original.setParkingTime(parkingTime.toSeconds()); //按秒计算
        //TODO 计算费用的方法
        order_original.setParkingFee((int)parkingTime.toMinutes()/120);
        return orderService.update(order_original);
    }
    @PostMapping("/update_orderPaid")
    public Order update_orderPaid(@RequestBody Order order){
        //TODO 校验
        //TODO 是否超时支付
        order.setOrderSubmissionTime(LocalDateTime.now());
        return orderService.update(order);
    }
    @PostMapping("/update_outGateInfo")
    public Order update_outGateInfo(@RequestBody Order order){
        //TODO 校验
        order.setOrderSubmissionTime(LocalDateTime.now());
        return orderService.update(order);
    }
    @PostMapping("/update_outOverTime")
    public Order update_outOverTime(@RequestBody Order order){
        //TODO 校验
        order.setOrderSubmissionTime(LocalDateTime.now());
        return orderService.update(order);
    }
    @PostMapping("/update_orderCompleted")
    public Order update_orderCompleted(@RequestBody Order order){
        //TODO 校验
        order.setOrderSubmissionTime(LocalDateTime.now());
//        order.setOutTime(LocalDateTime.now().plusSeconds(3659));
//        Duration duration = Duration.between(order.getInTime(), order.getOutTime());
//        order.setParkingTime(duration.toMinutes()); //不足一分钟的不计
        return orderService.update(order);
    }
}
