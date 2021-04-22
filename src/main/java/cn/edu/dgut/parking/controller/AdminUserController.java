package cn.edu.dgut.parking.controller;

import cn.edu.dgut.parking.model.AdminUser;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequestMapping("/adminUser")
@RestController
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/login")
    public Response<AdminUser> adminLogin(@RequestBody AdminUser adminUser, HttpServletResponse response){
//        response.setHeader("Access-Control-Allow-Origin", "*");
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
//        response.setHeader("Access-Control-Allow-Headers", "*");
        return adminUserService.adminLogin(adminUser);
    }

    @GetMapping("/getInfo")
    public Response<?> getAdminInfo(HttpServletRequest request){
        return adminUserService.getAdminInfo(request.getAttribute("claims").toString());
    }
    @PostMapping("/addAdmin")
    public Response<?> addAdmin(@RequestBody AdminUser adminUser, HttpServletRequest request){
        if (request.getAttribute("claims").equals("root")){
            return adminUserService.addAdmin(adminUser);
        }else{
            return Response.failuer("您没有对应权限",3005);
        }
    }

    @PostMapping("/update")
    public Response<?> update(@RequestBody AdminUser adminUser, HttpServletRequest request){
        if (request.getAttribute("claims").equals("root")){
            return adminUserService.update(adminUser);
        }else{
            return Response.failuer("您没有对应权限",3005);
        }
    }

    @PostMapping("/removeAdmin")
    public Response<?> removeAdmin(@RequestBody AdminUser adminUser, HttpServletRequest request){
        if (request.getAttribute("claims").equals("root")){
            return adminUserService.removeAdmin(adminUser);
        }else{
            return Response.failuer("您没有对应权限",3005);
        }
    }

    @GetMapping("/getAdminList")
    public Response<?> getAdminList(){
//        Pageable pageable = PageRequest.of(currentPage - 1, 10, Sort.by(Sort.Direction.DESC, "id"));
        return adminUserService.getAdminList();
    }
}
