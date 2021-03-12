package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.Test;
import cn.edu.dgut.parking.model.User;
import cn.edu.dgut.parking.repository.TestRepository;
import cn.edu.dgut.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private UserService userService;

    @PostMapping("/userLogin")
    public User userLogin(@RequestBody User user){
        return null;
    }
    @GetMapping("/")
    public Test test(){
        Test modtest = new Test();
        modtest.setName("bcc");
        modtest.setId(1222);
        modtest.setInTime(LocalDateTime.now());
        modtest.setOutTime(LocalDateTime.now().plusSeconds(3659));
        Duration duration = Duration.between(modtest.getInTime(), modtest.getOutTime());
        modtest.setTotalTime(duration.toMinutes()); //不足一分钟的不计
        return modtest;
    }
    @PostMapping("/add")
    public boolean add(@RequestBody Test test){
        testRepository.save(test);
        return true;
    }

}
