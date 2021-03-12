package cn.edu.dgut.parking.repository;

import cn.edu.dgut.parking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUid(String uid);
//    User findByOpenid(String openid);
}
