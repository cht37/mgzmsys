package cn.edu.neu.mgzmsys.service;

import cn.edu.neu.mgzmsys.entity.Child;
import cn.edu.neu.mgzmsys.entity.Users;
import cn.edu.neu.mgzmsys.entity.Volunteer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
public interface IUserService extends IService<Users> {
    //已经弃用
    //   String login(String username, String password);
/**
     * 注册业务
     * @return 是否成功
     */
    boolean register(Child child);
    boolean register(Volunteer volunteer);

    boolean updatePassword(String id, String password);

    Map<String,Object> selectUser(String name);
}
