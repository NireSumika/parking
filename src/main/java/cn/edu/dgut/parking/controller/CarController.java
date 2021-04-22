package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.Car;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.service.CarService;
import cn.edu.dgut.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping("/car")
@RestController
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;

    @PostMapping("/add_car")
    public Response<?> add_car(HttpServletRequest request, @RequestBody Car car){
        try {
            String openId = request.getAttribute("claims").toString();
            if (null == car.getLincesePlate()) return Response.failuer("车牌为空", 4004);
            return Response.withData(carService.add(openId, car));
        }catch (Exception e){
            e.printStackTrace();
            return Response.failuer("fail", 4010);
        }
    }
    @GetMapping("/get_car")
    public Response<Set> get_car(HttpServletRequest request){
        try {
            String openId = request.getAttribute("claims").toString();
            return Response.withData(carService.getCar(openId));
        }catch (Exception e){
            e.printStackTrace();
        }
        return new Response<Set>().setSuccess(false);
    }
    @GetMapping("/getCarList")
    public Response<?> getCarList(){
//        Pageable pageable = PageRequest.of(currentPage - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return carService.getCarList();
    }
    @GetMapping("/getCarByPlate")
    public Response<?> getCarListByPlate(@RequestParam String plate){
        return carService.getCarByPlate(plate);
    }

    @PostMapping("del_car")
    public Response<?> del_car(HttpServletRequest request, @RequestBody Car car){
        String openId = request.getAttribute("claims").toString();
        return Response.withData(carService.delCar(openId, car));
    }
}
