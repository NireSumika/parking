package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.*;
import cn.edu.dgut.parking.repository.OrderRepository;
import cn.edu.dgut.parking.repository.UserRepository;
import cn.edu.dgut.parking.util.GenerateOrderNum;
import cn.edu.dgut.parking.util.Global;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private CalculateFee calculateFee;
    @Autowired
    private OverTimeHandler overTimeHandler;
//    @Value("${overOutTime}")
//    private long overOutTime;
    @Autowired
    private Global global;
    //生成新订单
    public Long add(Order order){
//        Thread thread = new Thread(() -> {
        GenerateOrderNum generateOrderNum = new GenerateOrderNum();
        order.setOrderNum(generateOrderNum.generate());
//        });
        String path = order.getOrderNum() + "-in.jpg";
//        order.setInPicturePath(ImageTransfer.base64ToImg(order.getInPicturePath(), path));
//        thread.start();
        order.setInTime(LocalDateTime.now());
        Car carByPlate = carService.findCarByPlate(order.getLicensePlate());
        if (null != carByPlate){
            order.setCar(carByPlate);
            order.setUser(carByPlate.getUser());
        }
        orderRepository.save(order);
        return order.getId();
    }
    //通过uid查询订单
    public Set<Order> get_order_by_uid(String openId){
        try {
            Set<Order> orders = userService.findByUid(openId).getOrders();
            if (!orders.isEmpty()){
                return orders;
            } else {
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new HashSet<>();
    }

//    通过车牌号查询订单
    public Set<Order> get_order_by_plate(String openId, String plate){
        try {
            Set<Car> cars = userService.findByUid(openId).getCars();
            for (Car car : cars) {
                if (car.getLincesePlate().equals(plate)){
                    Set<Order> orders = car.getOrders();
                    if (!orders.isEmpty()){
                        return orders;
                    } else {
                        return null;
                    }
                }
            }
            return null;
        }catch (Exception e){
            e.printStackTrace();
        }
        return new HashSet<>();
    }
    //获取订单最新信息(小程序端)
    public Response<?> freshOrder(String orderNum, boolean member) throws JsonProcessingException {
        Order order = orderRepository.findByOrderNum(orderNum);
        LocalDateTime currentTime = LocalDateTime.now();
        if (null != order){
            if (order.getOrderCompleted()){
                return Response.withData(order);
            }
            //是否有超时离场记录
            if(0 == order.getOverTimeOut()){
                //未有超时离场记录，计算时长
                Duration calculateFreeTime = Duration.between(order.getInTime(), currentTime);
                //免费时长内，直接放行
                if (calculateFreeTime.toMinutes() <= global.freeTime){
                    order.setReleaseFlag(true);
                    order.setParkingTime(calculateFreeTime.toMinutes());
                    return Response.withData(order);
                }
                //超出免费时长，交由小程序暂时计算实时费用
                else {
                    //已支付
                    if (order.getPaid()) {
                        //判断是否支付后超时离场
                        Duration calculateOverTime = Duration.between(order.getOrderSubmissionTime(), currentTime);
                        //未超时离场，放行
                        if (calculateOverTime.toSeconds() <= global.outOverTime * 60) {
                            order.setReleaseFlag(true);
                            order.setParkingTime(calculateFreeTime.toMinutes());
                            return Response.withData(order);
                        }
                        //已超时，需支付超时订单
                        else {
//                            int outFlag = order.getOverTimeOut();
                            order.setOverTimeOut(1);
                            order.setReleaseFlag(false);
                            order.setOverTimeInfo(overTimeHandler.transToString(overTimeHandler.creatOverTimeOrder(order,0),false,false));
                            return Response.withData(order).setMessage("支付后未及时离场");
                        }
                    }
                    //未支付
                    else {
                        if (null != order.getOrderSubmissionTime()){
                            if (member){
                                order.setActualFee(0);
                                order.setReleaseFlag(true);
                                order.setParkingTime(calculateFreeTime.toMinutes());
                            } else {
                                order.setReleaseFlag(false);
                            }
                            return Response.withData(order);
                        }
                        order.setParkingFee(calculateFee.CalculateTempFee(order.getInTime()));
                        if (member){
                            order.setActualFee(0);
                            order.setReleaseFlag(true);
                            order.setParkingTime(calculateFreeTime.toMinutes());
                        } else {
                            order.setActualFee(order.getParkingFee());
                            order.setReleaseFlag(false);
                            order.setParkingTime(calculateFreeTime.toMinutes());
                        }
//                        orderRepository.save(order);
                        return Response.withData(order);
                    }


                }
            }
            //有超时离场记录，判断是否已支付超时费用
            else{
                OverTimeOrder overTimeOrder = overTimeHandler.transToObject(order.getOverTimeInfo());

                //已支付超时费用
                if (overTimeOrder.getOverTimeOutPaid()){
                    //判断是否支付后超时离场
                    Duration duration = Duration.between(overTimeOrder.getOverTimeOrderSubmitTime(), currentTime);
                    //未超时离场，放行
                    if (duration.toSeconds() <= global.outOverTime * 60) {
                        order.setReleaseFlag(true);
                    }else {
//                        overTimeOrder.setOverTimeTime((int)duration.toMinutes());
//                        overTimeOrder.setOverTimeOutPaid(false);
//                        overTimeOrder.setOverTimeOrderSubmitTime(LocalDateTime.now());
                        int outFlag = order.getOverTimeOut();
                        order.setOverTimeOut(outFlag);
                        order.setReleaseFlag(false);
                        order.setOverTimeInfo(overTimeHandler.transToString(overTimeHandler.creatOverTimeOrder(order,outFlag),false,false));
                    }
                }
                return Response.withData(order);
                //未支付

            }
        }
        return Response.failuer("失败", 4001);
    }


    //离场判断(重要)(识别端)
    public Response<?> outGate(Order order) throws JsonProcessingException {
        LocalDateTime outTime = LocalDateTime.now();
        Order judgeOrder = orderRepository.findByLicensePlateAndOrderCompleted(order.getLicensePlate(), false);
        if (null != judgeOrder){
            //是否有超时离场记录
            if(0 == judgeOrder.getOverTimeOut()){
                //未有超时离场记录，计算时长
                Duration calculateFreeTime = Duration.between(judgeOrder.getInTime(), outTime);
                //免费时长内，直接放行
                if (calculateFreeTime.toMinutes() <= global.freeTime){
                    judgeOrder.setReleaseFlag(true);
//                    orderRepository.save(judgeOrder);
                    return Response.withData(judgeOrder);
                }
                //超出免费时长，判断是否已支付
                else {
                    //已支付
                    if (judgeOrder.getPaid()) {
                        //判断是否支付后超时离场
                        Duration calculateOverTime = Duration.between(judgeOrder.getOrderSubmissionTime(), outTime);
                        //未超时离场，放行
                        if (calculateOverTime.toSeconds() <= global.outOverTime * 60) {

                            judgeOrder.setParkingTime(Duration.between(judgeOrder.getInTime(), LocalDateTime.now()).toMinutes());
                            judgeOrder.setReleaseFlag(true);
                            orderRepository.save(judgeOrder);
                        }
                        return Response.withData(judgeOrder);
                    }
                    //未支付
                    else {
                        User user = judgeOrder.getUser();
                        if (user.getMember() > 0){
                            LocalDateTime time = LocalDateTime.now();
                            Duration duration = Duration.between(time, user.getMemberTime());
                            if (duration.toMinutes() > 0){
                                judgeOrder.setReleaseFlag(true);
                            }else {
                                //会员已过期
                                user.setMember(0);
                                userRepository.save(user);
                            }
                        }
                        return Response.withData(judgeOrder);
                    }
                }
            }
            //有超时离场记录，判断是否已支付超时费用
            else{
                OverTimeOrder overTimeOrder = overTimeHandler.transToObject(judgeOrder.getOverTimeInfo());
                //已支付超时费用
                if (overTimeOrder.getOverTimeOutPaid()){
                    //判断是否支付后超时离场
                    Duration duration = Duration.between(overTimeOrder.getOverTimeOrderSubmitTime(), outTime);
                    //未超时离场，放行
                    if (duration.toSeconds() <= global.outOverTime * 60) {
                        judgeOrder.setReleaseFlag(true);
                    }
                }
                return Response.withData(judgeOrder);
            }
        }
        return Response.failuer("失败", 4001);
    }

    public Set<Order> findOrderByPlateManully(String openId, String plate){
        Set<Car> cars = userService.findByUid(openId).getCars();
        for (Car car : cars) {
            if (car.getLincesePlate().equals(plate)){
                Set<Order> orders = orderRepository.findByLicensePlate(plate);
                if (!orders.isEmpty()){
                    return orders;
                } else {
                    return null;
                }
            }
        }
        return null;
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
