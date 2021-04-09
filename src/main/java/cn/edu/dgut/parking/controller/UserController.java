package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.model.User;
import cn.edu.dgut.parking.repository.UserRepository;
import cn.edu.dgut.parking.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;

@RequestMapping("/user")
@RestController
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getUser")
    public Response<User> getUser(@RequestParam("uid") String uid){
        User user = userService.findByUid(uid);
        if (user.getMember() > 0){
            LocalDateTime time = LocalDateTime.now();
            Duration duration = Duration.between(time, user.getMemberTime());
            if (duration.toMinutes() > 0){
                return Response.withData(user);
            }else {
                //会员已过期
                user.setMember(0);
                userRepository.save(user);
                return Response.withData(user);
            }
        }
        return Response.withData(user);
    }
    @PostMapping("/putUser")
    public Boolean putUser(@RequestBody User userInfo){
        System.out.println(userInfo);
        return true;
    }
    /*@PostMapping("/userRegister")
    public User userRegister(@RequestBody User user){
        if(null == user.getId()){
            return userService.add(user);
        }
        return null;
    }*/
    @PostMapping("/userLogin")
    public User userLogin(@RequestBody User user){
        if(null != user.getCode()){
            return userService.login(user);
        }
        log.info("loginfail");
        return null;
    }
    @GetMapping("/becomeMember")
    public Response<?> becomeMember(HttpServletRequest request, @RequestParam("memberKind") int memberKind){
        String uid = request.getAttribute("claims").toString();
        return Response.withData(userService.becomeMember(uid, memberKind));
    }
}
