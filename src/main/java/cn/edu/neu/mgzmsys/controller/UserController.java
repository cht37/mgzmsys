package cn.edu.neu.mgzmsys.controller;


import cn.edu.neu.mgzmsys.common.utils.JwtUtil;
import cn.edu.neu.mgzmsys.common.utils.RedisUtil;
import cn.edu.neu.mgzmsys.entity.Child;
import cn.edu.neu.mgzmsys.entity.HttpResponseEntity;
import cn.edu.neu.mgzmsys.entity.MyUserDetails;
import cn.edu.neu.mgzmsys.entity.Volunteer;
import cn.edu.neu.mgzmsys.service.IChildService;
import cn.edu.neu.mgzmsys.service.IUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IUserService userService;
    @Resource
    private PasswordEncoder passwordEncoder;

    @PostMapping(value = "/register", headers = "Accept=application/json")
    public HttpResponseEntity register(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try {
            if (map == null) {
                throw new NullPointerException();
            }
            String type = (String) map.get("type");
            String encodedPassword = passwordEncoder.encode((String) map.get("password"));
            boolean result = false;
            //根据map new一个child对象
            if (type.equals("child")) {
                // 注册儿童
                // 注册逻辑...
                // 确保密码是加密过的
                Child child = new Child();
                child.setUserName((String) map.get("username"));
                child.setPassword(encodedPassword);
                child.setChildName((String) map.get("childName"));
                child.setBirthday(LocalDate.parse((String) map.get("birthday")));
                child.setHobby((String) map.get("hobby"));
                child.setAddress((String) map.get("address"));
                child.setGender((Integer) map.get("gender"));
                child.setPhone((String) map.get("phone"));
                child.setDescription((String) map.get("description"));
                result = userService.register(child);
            } else if (type.equals("volunteer")) {
                // 注册志愿者
                Volunteer volunteer = new Volunteer();
                result = userService.register(volunteer);
            }
            if (result) {
                return HttpResponseEntity.REGISTER_SUCCESS;
            } else {
                return HttpResponseEntity.REGISTER_FAIL;
            }
        } catch (Exception e) {
            // 异常处理...
            return HttpResponseEntity.ERROR;
        }
    }

    /**
     * 修改密码
     *
     * @return 修改是否成功
     */
    @PostMapping(value = "/updatePassword", headers = "Accept=application/json")
    public ResponseEntity<HttpResponseEntity> updatePassword(@RequestBody String password, @RequestHeader("token") String jwt) throws ParseException {
        // 使用Spring Security的Authentication对象来获取当前用户

        String userId = JwtUtil.getUidFromToken(jwt);
        try {
            // 加密新密码
            String encodedNewPassword = passwordEncoder.encode(password);
            boolean result = userService.updatePassword(userId, encodedNewPassword);
            // 更新密码逻辑...
            if (result) {
                return HttpResponseEntity.UPDATE_SUCCESS.toResponseEntity();
            } else {
                return HttpResponseEntity.UPDATE_FAIL.toResponseEntity();
            }
        } catch (Exception e) {
            // 异常处理...
            return HttpResponseEntity.ERROR.toResponseEntity();
        }
    }

    /**
     * 查询用户信息
     *
     * @return 用户信息
     */
    @PostMapping(value = "/searchUserInfo", headers = "Accept=application/json")
    public ResponseEntity<HttpResponseEntity> getUserInfo(@RequestBody String name) {
        try {
            // 查询用户信息逻辑...
            Map<String, Object> userInfo = userService.selectUser(name);
            return new HttpResponseEntity().get(userInfo).toResponseEntity();
        } catch (Exception e) {
            // 异常处理...
            return HttpResponseEntity.ERROR.toResponseEntity();
        }
    }

    @PostMapping(value = "/logout", headers = "Accept=application/json")
    public ResponseEntity<HttpResponseEntity> logout(HttpServletRequest request) {
        try {
            // 查询用户信息逻辑...
            String token = request.getHeader("token");
            String userId = JwtUtil.getUidFromToken(token);

            String key = "login:" + userId;
            redisUtil.del(key);
            MyUserDetails loginUser = (MyUserDetails) redisUtil.get(key);
            if (Objects.isNull(loginUser)) {
                return HttpResponseEntity.LOGOUT_SUCCESS.toResponseEntity();
            } else {
                return HttpResponseEntity.LOGOUT_FAIL.toResponseEntity();
            }
        } catch (Exception e) {
            // 异常处理...
            return HttpResponseEntity.ERROR.toResponseEntity();
        }
    }
}

