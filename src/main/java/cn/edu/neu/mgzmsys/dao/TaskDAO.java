package cn.edu.neu.mgzmsys.dao;

import cn.edu.neu.mgzmsys.entity.Task;
import cn.edu.neu.mgzmsys.mapper.TaskMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class TaskDAO {
    @Resource
    private TaskMapper taskMapper;
    public List<Task> getTaskById(String id){
         LambdaQueryWrapper<Task> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        String sql = "select task_id from child_task where child_id = \"" + id+"\"";
        lambdaQueryWrapper.inSql(Task::getTaskId, sql);
        return taskMapper.selectList(lambdaQueryWrapper);
    }
}
