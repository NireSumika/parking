package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.Car;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.service.CarService;
import cn.edu.dgut.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

@RequestMapping("/car")
@RestController
public class CarController {
    @Autowired
    private CarService carService;
    @Autowired
    private UserService userService;

    @PostMapping("/add_car")
    public Response<Boolean> add_car(HttpServletRequest request, @RequestBody Car car){
        try {
            String openId = request.getAttribute("claims").toString();
            car.setUser(userService.findByUid(openId));
            return Response.withData(carService.add(openId, car));
        }catch (Exception e){
            e.printStackTrace();
            return new Response<Boolean>().setCode(205).setMessage("fail").setSuccess(false);
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
    @PostMapping("del_car")
    public Response<?> del_car(HttpServletRequest request, @RequestBody Car car){
        String openId = request.getAttribute("claims").toString();
        return Response.withData(carService.delCar(openId, car));
    }
}
