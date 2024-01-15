package cn.edu.neu.mgzmsys.dao;

import cn.edu.neu.mgzmsys.entity.TaskChild;
import cn.edu.neu.mgzmsys.mapper.TaskChildMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class ChildTaskDAO {
    @Resource
    private TaskChildMapper taskChildMapper;
    public TaskChild getTaskChildByMap(Map<String, Object> map){
         return taskChildMapper.selectChildTask(map);
    }
    public boolean updateChildTask(Map<String, Object> map){
        return taskChildMapper.updateChildTask(map);
    }
}
