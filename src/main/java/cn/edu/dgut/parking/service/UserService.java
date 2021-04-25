package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.Response;
import cn.edu.dgut.parking.model.User;
import cn.edu.dgut.parking.model.UserOpenId;
import cn.edu.dgut.parking.repository.UserRepository;
import cn.edu.dgut.parking.util.HttpUtil;
import cn.edu.dgut.parking.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;


@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private UserOpenId getWxOpenid(User user){
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        String appid = "appid=wxb9286d606d58f370";
        String secret = "&secret=7205b83a144f6d333f43f1791559665b";
        String result = HttpUtil.sendGet(url, appid + secret + "&js_code=" + user.getCode() + "&grant_type=authorization_code");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(result, UserOpenId.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }
    public User findByUid(String uid){
        return userRepository.findByUid(uid);
    }
//    public User add(User user){
//        UserOpenId userOpenId = getWxOpenid(user);
//        if (userOpenId != null) {
//            user.setUid(userOpenId.getOpenid());
//            user.setSession_key(userOpenId.getSession_key());
//            user = userRepository.save(user);
//            System.out.println(user);
//            return user;
//        }
//        return null;
//    }

    public User login(User user){
        UserOpenId userOpenId = getWxOpenid(user);
        if (userOpenId != null){
            if(null == userRepository.findByUid(userOpenId.getOpenid())) {
                user.setUid(userOpenId.getOpenid());
                user.setSession_key(userOpenId.getSession_key());
                userRepository.save(user);
            }
            if (null == user.getToken() || null == TokenUtil.verifyToken(user.getToken())){
                user.setToken(TokenUtil.createToken(userOpenId.getOpenid()));
            }
            User byUid = userRepository.findByUid(userOpenId.getOpenid());
            byUid.setToken(user.getToken());
            if (user.getMember() > 0){
                LocalDateTime time = LocalDateTime.now();
                Duration duration = Duration.between(time, user.getMemberTime());
                if (duration.toMinutes() <= 0) {
                    user.setMember(0);
                    userRepository.save(user);
                }
            }
            return byUid.copyUser();
        }
        return null;
    }

    public User becomeMember(String uid, int memberKind){
        User user = userRepository.findByUid(uid);
        Map<Integer, Integer> dic = new HashMap<>();
        dic.put(1, 30);
        dic.put(2, 90);
        dic.put(3, 365);
        if (user.getMember() > 0){
            LocalDateTime time = LocalDateTime.now();
            Duration duration = Duration.between(time, user.getMemberTime());
            if (duration.toMinutes() > 0){
                user.setMemberTime(user.getMemberTime().plusDays(dic.get(memberKind)));
                return userRepository.save(user);
            }
        }
        user.setMember(1);
        user.setMemberTime(LocalDateTime.now().plusDays(dic.get(memberKind)));
        return userRepository.save(user);
    }

    public Response<?> getUserList(){
        return Response.withData(userRepository.findAll());
    }
}
