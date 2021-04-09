package cn.edu.dgut.parking.repository;

import cn.edu.dgut.parking.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, String> {
    AdminUser findByAdminId(String adminId);
}
