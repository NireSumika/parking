package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.*;
import cn.edu.dgut.parking.model.Order;
import cn.edu.dgut.parking.repository.OrderRepository;
import cn.edu.dgut.parking.repository.ParkingLotRepository;
import cn.edu.dgut.parking.repository.UserRepository;
import cn.edu.dgut.parking.service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequestMapping("/order")
@RestController

public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CalculateFee calculateFee;
    @Autowired
    private OverTimeHandler overTimeHandler;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CarService carService;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
//    @GetMapping("/")
//    public Order order(@RequestParam("id") Long id){
//        return orderService.findById(id);
//    }

    @PostMapping("/add")
    public Response<?> add(@RequestBody Order order){
        if (null == order.getLicensePlate() || null == order.getParkingLotName()){
            return Response.failuer("无车辆数据", 4006);
        }
        Order newOrder = new Order();
        Order judgeOrder = orderRepository.findByLicensePlateAndOrderCompleted(order.getLicensePlate(), false);
        System.out.println(judgeOrder);
        if(null != judgeOrder){
            return Response.failuer("存在未完成订单", 4011);
        }
        newOrder.setInGate(order.getInGate());
        newOrder.setParkingLotName(order.getParkingLotName());
        newOrder.setLicensePlate(order.getLicensePlate());
        newOrder.setInPassWay(order.getInPassWay());
        ParkingLot parkingLot = parkingLotRepository.findByParkingLotName(order.getParkingLotName());
        if (null != parkingLot) {
            if (parkingLot.getRemaining() <= 0){
                return Response.failuer("无剩余车位", 4009);
            }
            parkingLot.setRemaining(parkingLot.getRemaining() - 1);
        }
        parkingLotRepository.save(parkingLot);
//        newOrder.setInPicturePath(order.getInPicturePath());
        return Response.withData(orderService.add(newOrder));
    }

    @PostMapping("/add_member_order")
    public Response<?> add_member_order(@RequestBody Order order) {
        if (null == order.getLicensePlate() || null == order.getParkingLotName()){
            return Response.failuer("无车辆数据", 4006);
        }
        User tempuser = carService.findCarByPlate(order.getLicensePlate()).getUser();
        if (null == tempuser || tempuser.getMember() == 0){
            return Response.failuer("非会员不可进入", 3008);
        }
        Order newOrder = new Order();
        newOrder.setInGate(order.getInGate());
        newOrder.setParkingLotName(order.getParkingLotName());
        newOrder.setLicensePlate(order.getLicensePlate());
        newOrder.setInPassWay(order.getInPassWay());
        ParkingLot parkingLot = parkingLotRepository.findByParkingLotName(order.getParkingLotName());
        if (null != parkingLot) {
            if (parkingLot.getRemaining() <= 0){
                return Response.failuer("无剩余车位", 4009);
            }
            parkingLot.setRemaining(parkingLot.getRemaining() - 1);
        }
        parkingLotRepository.save(parkingLot);
//        newOrder.setInPicturePath(order.getInPicturePath());
        return Response.withData(orderService.add(newOrder));
    }

    //离场(重要)(识别端)
    @PostMapping("/update_memberOutGateInfo")
    public Response<?> update_memberOutGateInfo(@RequestBody Order order) throws JsonProcessingException {
        Order original = orderRepository.findByLicensePlateAndOrderCompleted(order.getLicensePlate(), false);
        if (null != original){
            original.setReleaseFlag(true);
            orderRepository.save(original);
            return Response.withData(original);
        }
        return Response.failuer("无订单数据", 4004);
    }

    @GetMapping("/get_order_by_uid")
    public Response<Set> get_order_by_uid(HttpServletRequest request){
        try {
            String openId = request.getAttribute("claims").toString();
            return Response.withData(orderService.get_order_by_uid(openId));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<Set>().setSuccess(false);
    }
    @GetMapping("/get_order_by_plate")
    public Response<Set> get_order_by_plate(HttpServletRequest request, @RequestParam("plate") String plate){
        try {
            String openId = request.getAttribute("claims").toString();
            return Response.withData(orderService.get_order_by_plate(openId, plate));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<Set>().setSuccess(false);
    }
    @GetMapping("/getAll")
    public Response<?> getAllPage(HttpServletRequest request){//, @RequestParam("page") Integer currentPage){
        return Response.withData(orderRepository.findAll(
                PageRequest.of(0, 100000, Sort.by(Sort.Direction.DESC, "id"))));
    }
    @GetMapping("/getUserAllPage")
    public Response<Page<Order>> getUserAllPage(HttpServletRequest request, @RequestParam("page") Integer currentPage){
        String uid = request.getAttribute("claims").toString();
        User user = userService.findByUid(uid);
        Pageable pageable = PageRequest.of(currentPage - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return Response.withData(orderRepository.findAllByUser(user, pageable));
    }
    @GetMapping("/getParkingLotAllPage")
    public Response<Page<Order>> getParkingLotAllPage(HttpServletRequest request, @RequestParam("page") Integer currentPage, @RequestParam("plate") String parkingLot){
//        String uid = request.getAttribute("claims").toString();
//        User user = userService.findByUid(uid);
//        Car car = carService.findCarByPlate(plate);
        Pageable pageable = PageRequest.of(currentPage - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return Response.withData(orderRepository.findAllByParkingLotName(parkingLot, pageable));
    }
    @GetMapping("/getCarAllPage")
    public Response<Page<Order>> getCarAllPage(@RequestParam("page") Integer currentPage, @RequestParam("plate") String plate){
//        String uid = request.getAttribute("claims").toString();
//        User user = userService.findByUid(uid);
//        Car car = carService.findCarByPlate(plate);
        Pageable pageable = PageRequest.of(currentPage - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return Response.withData(orderRepository.findAllByLicensePlate(plate, pageable));
    }
    @GetMapping("/getCompletedAllPage")
    public Response<Page<Order>> getCompletedAllPage(HttpServletRequest request, @RequestParam("page") Integer currentPage, @RequestParam("plate") Boolean completed){
//        String uid = request.getAttribute("claims").toString();
//        User user = userService.findByUid(uid);
//        Car car = carService.findCarByPlate(plate);
        Pageable pageable = PageRequest.of(currentPage - 1, 15, Sort.by(Sort.Direction.DESC, "id"));
        return Response.withData(orderRepository.findAllByOrderCompleted(completed, pageable));
    }
//    获取订单最新信息(小程序端)
    @PostMapping("/fresh_order")
    public Response<?> fresh_order(@RequestBody Order order, HttpServletRequest request) throws JsonProcessingException {
        String openId = request.getAttribute("claims").toString();
        User user = userService.findByUid(openId);
        if (user.getMember() > 0){
            LocalDateTime time = LocalDateTime.now();
            Duration duration = Duration.between(time, user.getMemberTime());
            if (duration.toMinutes() > 0){
                return orderService.freshOrder(order.getOrderNum(), true);
            }else {
                //会员已过期
                user.setMember(0);
                userRepository.save(user);
                return orderService.freshOrder(order.getOrderNum(), false);
            }
        }
        return orderService.freshOrder(order.getOrderNum(),false);
    }
    @PostMapping("/update_orderSubmissionTime")
    public Response<?> update_orderSubmissionTime(@RequestBody Order order){
        Order order_original = orderService.findByOrderNum(order.getOrderNum());
        order_original.setOrderSubmissionTime(LocalDateTime.now());
        Duration parkingTime = Duration.between(order_original.getInTime(), order_original.getOrderSubmissionTime());
        order_original.setParkingTime(parkingTime.toMinutes()); //按分计算
        order_original.setParkingFee(calculateFee.CalculateNormalFee(order_original));
        order_original.setReleaseFlag(false);
        return Response.withData(orderService.update(order_original));
    }

    @PostMapping("/update_orderPaid")
    public Response<?> update_orderPaid(@RequestBody Order order){
        Order order_original = orderService.findByOrderNum(order.getOrderNum());
        order_original.setPaid(true);
        order_original.setPayTime(LocalDateTime.now());
        return Response.withData(orderService.update(order_original));
    }

    @PostMapping("/update_overTimeOrderSubmissionTime")
    public Response<?> update_overTimeOrderSubmissionTime(@RequestBody Order order) throws JsonProcessingException {
        Order order_original = orderService.findByOrderNum(order.getOrderNum());
        int outFlag = order_original.getOverTimeOut();
        order_original.setOverTimeOut(outFlag + 1);
        order_original.setReleaseFlag(false);
        order_original.setOverTimeInfo(overTimeHandler.transToString(overTimeHandler.creatOverTimeOrder(order_original, outFlag + 1), true, false));
        return Response.withData(orderService.update(order_original));
    }

    @PostMapping("/update_overTimeOrderPaid")
    public Response<?> update_overTimeOrderPaid(@RequestBody Order order) throws JsonProcessingException {
        Order order_original = orderService.findByOrderNum(order.getOrderNum());
        OverTimeOrder overTimeOrder = overTimeHandler.transToObject(order_original.getOverTimeInfo());
        order_original.setOverTimeInfo(overTimeHandler.transToString(overTimeOrder, false, true));
        return Response.withData(orderService.update(order_original));
    }

    //离场(重要)(识别端)
    @PostMapping("/update_outGateInfo")
    public Response<?> update_outGateInfo(@RequestBody Order order) throws JsonProcessingException {
        //TODO 校验
        Order newOrder = new Order();
        newOrder.setLicensePlate(order.getLicensePlate());
        return orderService.outGate(newOrder);
    }

    //放行后更新数据
    @PostMapping("/update_release")
    public Response<?> updata_release(@RequestBody Order order){
        Order order_original = orderService.findByOrderNum(order.getOrderNum());
        order_original.setOutTime(LocalDateTime.now());
        order_original.setOutGate(order.getOutGate());
        order_original.setOutPassWay(order.getOutPassWay());
        order_original.setOutPicturePath(order.getOutPicturePath());
        order_original.setReleaseFlag(true);
        order_original.setOrderCompleted(true);
        ParkingLot parkingLot = parkingLotRepository.findByParkingLotName(order_original.getParkingLotName());
        if (null != parkingLot) {
            parkingLot.setRemaining(parkingLot.getRemaining() + 1);
        }
        parkingLotRepository.save(parkingLot);
        String path = order.getOrderNum() + "-out.jpg";
//        order_original.setOutPicturePath(ImageTransfer.base64ToImg(order.getOutPicturePath(), path));
        orderService.update(order_original);
        return Response.success();
    }
}
