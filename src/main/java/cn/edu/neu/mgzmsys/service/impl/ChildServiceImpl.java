package cn.edu.neu.mgzmsys.service.impl;

import cn.edu.neu.mgzmsys.common.utils.RedisUtil;
import cn.edu.neu.mgzmsys.dao.ChildDAO;
import cn.edu.neu.mgzmsys.entity.Child;
import cn.edu.neu.mgzmsys.mapper.ChildMapper;
import cn.edu.neu.mgzmsys.service.IChildService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 儿童 服务实现类
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
@Service
public class ChildServiceImpl extends ServiceImpl<ChildMapper, Child> implements IChildService {

    @Resource
    ChildDAO childDAO;
    @Resource
    RedisUtil redisUtil;

 /**
     * 查询儿童信息
     * @return 儿童信息
     */
    @Override
    public Child selectChildInfo(String id) {

        // 从redis中获取儿童信息
        Child child = (Child) redisUtil.get("child:" + id);
        if ( child != null ) {
            return child;
        }
        // 从数据库中获取儿童信息
        child = childDAO.getChildById(id);
        // 将儿童信息存入redis
        redisUtil.set("child:" + id, child);
        return child;
    }
    /**
     * 更新儿童信息
     * @return 是否成功
     */
    @Override
    public boolean updateChildInfo(Child child) {
           // 更新数据库中的儿童信息
            boolean result = childDAO.updateChildInfo(child);
            // 更新redis中的儿童信息
            redisUtil.set("child:" + child.getUserId(), child);
            return result;
    }
}
