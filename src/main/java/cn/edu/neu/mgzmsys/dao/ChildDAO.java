package cn.edu.neu.mgzmsys.dao;

import cn.edu.neu.mgzmsys.entity.Child;
import cn.edu.neu.mgzmsys.mapper.ChildMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class ChildDAO
{
    @Resource
    private ChildMapper childMapper;
    public Child getChildById(String id)
    {
        QueryWrapper<Child> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id", "child_name", "gender", "birthday", "address", "phone", "hobby", "description");
        return  childMapper.selectOne(queryWrapper.eq("user_id", id));
    }
    public boolean updateChildInfo(Child child){
         QueryWrapper<Child> wrapper = new QueryWrapper<>();
        //生成update child_name,gender,birthday,address,phone,hobby,description from child where user_id = #{userId}的wrapper
        wrapper.eq("user_id", child.getUserId());

        return childMapper.update(child, wrapper) == 1;
    }
}
