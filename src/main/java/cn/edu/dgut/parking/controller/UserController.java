package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.model.User;
import cn.edu.dgut.parking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/getUser")
    public Response<User> getUser(@RequestParam("uid") String uid){
        return Response.withData(userService.findByUid(uid));
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
        return null;
    }
}
