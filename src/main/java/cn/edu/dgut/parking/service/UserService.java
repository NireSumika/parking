package cn.edu.dgut.parking.service;

import cn.edu.dgut.parking.model.User;
import cn.edu.dgut.parking.model.UserOpenId;
import cn.edu.dgut.parking.repository.UserRepository;
import cn.edu.dgut.util.HttpUtil;
import cn.edu.dgut.util.TokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
            user.setToken(TokenUtil.createToken(userOpenId.getOpenid()));
            if(null == userRepository.findByUid(userOpenId.getOpenid())) {
                user.setUid(userOpenId.getOpenid());
                user.setSession_key(userOpenId.getSession_key());
                return userRepository.save(user);
            }
            User byUid = userRepository.findByUid(userOpenId.getOpenid());
            byUid.setToken(user.getToken());
            return byUid;
        }
        return null;
    }
}
