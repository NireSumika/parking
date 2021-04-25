package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.Car;
import cn.edu.dgut.parking.model.Order;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.model.User;
import cn.edu.dgut.parking.repository.CarRepository;
import cn.edu.dgut.parking.repository.OrderRepository;
import cn.edu.dgut.parking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class CarService {
    @Autowired
    private CarRepository carRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    public boolean add(String openId, Car car){
        car.setUser(userService.findByUid(openId));
        Set<Order> orders = findOrdersByPlate(car.getLincesePlate());
        if (null != orders){
            User user = userService.findByUid(openId);
            for (Order order : orders) {
                order.setCar(car);
                order.setUser(user);
            }
            orderRepository.saveAll(orders);
        }
        carRepository.save(car);
        return true;
    }
    public Set<Car> getCar(String openId){
        try {
//            Set<Car> carSet = new HashSet<>();
//            for (Car car : cars) {
//                carSet.add(car.copyCar());
//            }
//            return carSet;
            return userService.findByUid(openId).getCars();
        }catch (Exception e){
            e.printStackTrace();
        }
        return new HashSet<>();
    }
    public Response<?> delCar(String openId, Car delcar){
        Car originCar = carRepository.findByLincesePlate(delcar.getLincesePlate());
        if (originCar.getLincesePlate() != null){
            Set<Car> user_cars = originCar.getUser().getCars();
            for (Car carItem:user_cars){
                if (carItem.getLincesePlate().equals(originCar.getLincesePlate())){
                    user_cars.remove(carItem);
                }
            }
            originCar.getUser().setCars(user_cars);
            carRepository.save(originCar);
            for (Order orderItem: originCar.getOrders()){
                orderItem.setCar(null);
                orderRepository.save(orderItem);
            }
        }
//        Set<Car> cars = userService.findByUid(openId).getCars();
//        if (null != cars){
////            User user = userService.findByUid(openId);
//            for (Car car : cars) {
//                if (car.getLincesePlate().equals(delcar.getLincesePlate())){
//                    carRepository.delete(car);
//                    return Response.success();
//                }
//            }
//        }
        return Response.failuer("fail", 5002);
    }

    public Response<?> getCarList(){
        return Response.withData(carRepository.findAll());
    }

    public Car findCarByPlate(String plate){
        return carRepository.findByLincesePlate(plate);
    }

    public Response<?> getCarByPlate(String plate){
        return Response.withData(carRepository.findByLincesePlate(plate));
    }
    private Set<Order> findOrdersByPlate(String plate){
        return orderRepository.findByLicensePlate(plate);
    }
}
