package cn.edu.neu.mgzmsys.handler;

import cn.edu.neu.mgzmsys.common.utils.JwtUtil;
import cn.edu.neu.mgzmsys.common.utils.RedisUtil;
import cn.edu.neu.mgzmsys.entity.MyUserDetails;
import cn.edu.neu.mgzmsys.entity.Users;
import cn.edu.neu.mgzmsys.service.impl.SecurityService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private SecurityService securityService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }

    /**
     * StringRedisTemplate和RedisTemplate
     */
    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        /*
         * 1、判断请求头是否携带jwt
         *   否：放行不处理
         *   是：走到第二步
         */
        String jwt = httpServletRequest.getHeader("token");
        if (jwt == null) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);//交给过滤器处理
            return;
        }
        /*
         * 2、对前端传过来的jwt解密
         *   否：放行不处理
         *   是：走到第三步
         */
        if (!JwtUtil.decode(jwt)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        /*
         * 3、从jwt中获取用户信息
         *   否：放行不处理
         *   是：走到第四步
         */
        Map payLoad = JwtUtil.getPayLoad(jwt);
        String username = (String) payLoad.get("username");
        String userId = (String) payLoad.get("uid");
        /*
         * 4、根据用户名查询用户信息
         *   否：放行不处理
         *   是：走到第五步
         */
           // 获取userid 从redis中获取用户信息
        String redisKey = "login:" + userId;
        MyUserDetails loginUser = (MyUserDetails) redisUtil.get(redisKey);
        if (Objects.isNull(loginUser)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
        //把用户信息放到security容器中
        MyUserDetails userDetails = securityService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken upa = new UsernamePasswordAuthenticationToken(loginUser, null,
                userDetails.getAuthorities());
        //把信息放到security容器中
        SecurityContextHolder.getContext().setAuthentication(upa);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }


}

