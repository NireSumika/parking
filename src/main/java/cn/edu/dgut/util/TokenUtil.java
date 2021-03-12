package cn.edu.dgut.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TokenUtil {
    private static final long EXPIRE_TIME = 24 * 3600 * 1000;//默认1天
    //私钥
    private static final String TOKEN_SECRET = "JFI39T4jfe9DF3fjiWji3ijnlgLJIE8fniSFEiji3Si";

    /**
     * 生成签名
     * @param **username**
     * @param **password**
     * @return
     */
    public static String createToken(String userId) {
        try {
            // 设置过期时间
            Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            // 私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            // 设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("Type", "Jwt");
            header.put("alg", "HS256");
            // 返回token字符串
            return JWT.create()
                    .withHeader(header)
                    .withClaim("userId", userId)
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    /**
//     * 生成token，自定义过期时间 毫秒
//     * @param **username**
//     * @param **password**
//     * @return
//     */
//    public static String createToken(String userId,long expireDate) {
//        try {
//            // 设置过期时间
//            Date date = new Date(System.currentTimeMillis() + expireDate);
//            // 私钥和加密算法
//            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
//            // 设置头部信息
//            Map<String, Object> header = new HashMap<>(2);
//            header.put("Type", "Jwt");
//            header.put("alg", "HS256");
//            // 返回token字符串
//            return JWT.create()
//                    .withHeader(header)
//                    .withClaim("userId", userId)
//                    .withExpiresAt(date)
//                    .sign(algorithm);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    /**
     * 检验token是否正确
     * @param **token**
     * @return
     */
    public static String verifyToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim("userId").asString();
        } catch (Exception e){
            return null;
        }
    }
    public static void main(String[] args) {
        String token = TokenUtil.createToken("z");
        System.out.println("token == " + token);
        String userId = TokenUtil.verifyToken(token);
        System.out.println("userId == " + userId);

        //新建定时任务
//        Runnable runnable = new Runnable() {
//            //run方法中是定时执行的操作
//            public void run() {
//                System.out.println(new Date());
//                String userId = TokenUtil.verifyToken(token);
//                System.out.println("userId == " + userId);
//            }
//        };
//        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
//        /*
//         * 参数一:command：执行线程
//         * 参数二:initialDelay：初始化延时
//         * 参数三:period：两次开始执行最小间隔时间
//         * 参数四:unit：计时单位
//         */
//        service.scheduleAtFixedRate(runnable, 0, 4, TimeUnit.SECONDS);
    }
}
