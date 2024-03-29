package cn.edu.neu.mgzmsys.handler;

import cn.edu.neu.mgzmsys.common.utils.JwtUtil;
import cn.edu.neu.mgzmsys.common.utils.RedisUtil;
import cn.edu.neu.mgzmsys.entity.HttpResponseEntity;
import cn.edu.neu.mgzmsys.entity.MyUserDetails;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理器
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    private RedisUtil redisUtil;
    @SneakyThrows//注解抛异常
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        MyUserDetails user=(MyUserDetails) authentication.getPrincipal();
        String username=user.getUsername();
        String uid=user.getUid();
        String jwt= JwtUtil.createToken(username,uid);
         Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("userId", uid);
        payloadMap.put("userName", user.getUsername());
        payloadMap.put("token", jwt);

        boolean resultRedis = redisUtil.set("login:" + uid, user);

        if(!resultRedis){
            throw new RuntimeException("redis连接不上，登录失败");
        }

        httpServletResponse.getWriter().write(JSON.toJSONString(new HttpResponseEntity().ok(jwt)));
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
    }
}