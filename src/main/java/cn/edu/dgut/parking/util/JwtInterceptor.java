package cn.edu.dgut.parking.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 *  自定义拦截器 ，作用不用再在每个方法前都去解析token了
 *  1.在preHandle中，可以进行编码、安全控制等处理；进入到api接口之前要做的事 必须要有布尔类型的返回值
 *      true 表示可以继续执行
 *      false 拦截了表示 不能往下执行了
 */
@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle被调用");
        log.info(request.getRequestURI());
        String token = request.getHeader("token");
        if(null != token){
            String verifyToken = TokenUtil.verifyToken(token);
            if (null != verifyToken) {
                request.setAttribute("claims", verifyToken);
                return true;
            }
        }
        //表示该用户没有登录，输出 出错信息
        PrintWriter out = null;
        try{
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            out = response.getWriter();
            out.append("{\"success\":false,\"code\":\"notLogin\",\"msg\":\"您已经被拦截了，请重新登录\"}");
//            response.sendError(10001, "用户未登录");
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (out != null) {
                out.close();
            }
        }
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

}
