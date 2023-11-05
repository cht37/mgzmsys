package cn.edu.neu.mgzmsys.controller;


import cn.edu.neu.mgzmsys.common.utils.JwtUtil;
import cn.edu.neu.mgzmsys.entity.HttpResponseEntity;
import cn.edu.neu.mgzmsys.service.IChildService;
import cn.edu.neu.mgzmsys.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
@RestController
@RequestMapping("/user")
public class UserController {
 @Resource
    private IUserService userService;

    @PostMapping(value = "/login", headers = "Accept=application/json")
    public HttpResponseEntity login(@RequestBody Map<String,Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String username=map.get("username").toString();
        String password=map.get("password").toString();
        try{
            if ( username == null || password == null ) {
                throw new NullPointerException();
            }
            String uid = userService.login(username, password);
            if ( uid!=null ) {
                httpResponseEntity.setToken(JwtUtil.createToken(username,uid));
                httpResponseEntity.setCode("1");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("登录成功");
            } else {
                httpResponseEntity.setCode("0");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("用户名或密码错误");
            }
        } catch ( Exception e ) {
            httpResponseEntity.setCode("-1");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("登录时发生异常，请稍后重试");
        }
        return httpResponseEntity;
    }

    @PostMapping(value = "/register", headers = "Accept=application/json")
    public HttpResponseEntity register(@RequestBody Map<String, Object> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            if ( map == null ) {
                throw new NullPointerException();
            }
            String type = (String) map.get("type");
            if ( type.equals("child") ) {
                // 注册儿童
                httpResponseEntity.setCode("1");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("注册成功");
            } else if ( type.equals("volunteer") ) {
                // 注册志愿者
                httpResponseEntity.setCode("1");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("注册成功");
            } else {
                httpResponseEntity.setCode("0");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("注册失败");
            }
        } catch ( Exception e ) {
            httpResponseEntity.setCode("-1");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("注册时发生异常，请稍后重试");
        }
        return httpResponseEntity;
    }
    /**
     * 修改密码
     * @return 修改是否成功
     */
    @PostMapping(value = "/updatePassword", headers = "Accept=application/json")
    public HttpResponseEntity updatePassword(@RequestBody String password,@RequestHeader ("token")String token) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        try{
            String id = JwtUtil.getUidFromToken(token);
            boolean result = userService.updatePassword(id, password);
            if ( result ) {
                httpResponseEntity.setCode("1");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("修改密码成功");
            } else {
                httpResponseEntity.setCode("0");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("修改密码失败");
            }
        } catch ( Exception e ) {
            httpResponseEntity.setCode("-1");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("修改密码时发生异常，请稍后重试");
        }
        return httpResponseEntity;
    }

}

