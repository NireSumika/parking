package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.AdminUser;
import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.repository.AdminUserRepository;
import cn.edu.dgut.parking.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminUserService {
    @Autowired
    private AdminUserRepository adminUserRepository;

    public Response<AdminUser> adminLogin(AdminUser adminUser){
        if (null == adminUserRepository.findByAdminId("root")){
            AdminUser rootAdmin = new AdminUser();
            rootAdmin.setAdminId("root");
            rootAdmin.setPassWd("root");
            rootAdmin.setNickName("系统管理员");
            adminUserRepository.save(rootAdmin);
        }
        AdminUser getUser = adminUserRepository.findByAdminId(adminUser.getAdminId());
        if (null != getUser){
            if (adminUser.getPassWd().equals(getUser.getPassWd())){
                if (adminUser.getAdminId().equals("root")){
                    getUser.setToken(TokenUtil.createToken(getUser.getAdminId()));
                } else {
                    getUser.setToken(TokenUtil.createAdminToken(getUser.getAdminId()));
                }
                return Response.withData(getUser);
            }
            return Response.failuer("密码错误",3002);
        }
        return Response.failuer("用户不存在",3001);
    }
    public Response<?> addAdmin(AdminUser adminUser){
        if (null == adminUserRepository.findByAdminId(adminUser.getAdminId())){
            return Response.withData(adminUserRepository.save(adminUser));
        }else {
            return Response.failuer("该用户账号已存在", 3004);
        }
    }
    public Response<?> removeAdmin(AdminUser adminUser){
        if (null != adminUserRepository.findByAdminId(adminUser.getAdminId())){
            adminUserRepository.delete(adminUser);
            return Response.success();
        }else {
            return Response.failuer("该用户账号不存在", 3001);
        }
    }

    public Response<?> getAdminInfo(String claims) {
        return Response.withData(adminUserRepository.findByAdminId(claims));
    }
}
