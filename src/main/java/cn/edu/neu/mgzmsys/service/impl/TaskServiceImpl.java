package cn.edu.neu.mgzmsys.service.impl;

import cn.edu.neu.mgzmsys.common.utils.RedisUtil;
import cn.edu.neu.mgzmsys.dao.ChildTaskDAO;
import cn.edu.neu.mgzmsys.dao.TaskDAO;
import cn.edu.neu.mgzmsys.entity.Task;
import cn.edu.neu.mgzmsys.entity.TaskChild;
import cn.edu.neu.mgzmsys.mapper.TaskChildMapper;
import cn.edu.neu.mgzmsys.mapper.TaskMapper;
import cn.edu.neu.mgzmsys.service.ITaskService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Resource
    TaskMapper taskMapper;
    @Resource
    TaskDAO taskDAO;
    @Resource
    RedisUtil redisUtil;
    @Resource
    ChildTaskDAO childTaskDAO;

    /**
     * 根据儿童id获取任务
     * @return 任务列表
     */
    @Override
    public List<Task> getTaskById(String id){
      //从redis中获取任务
        List<Task> tasks = (List<Task>) redisUtil.get("task:" + id);
        if (tasks != null) {
            return tasks;
        }
        //从数据库中获取任务
        tasks = taskDAO.getTaskById(id);
        //将任务存入redis
        redisUtil.set("task:" + id, tasks);
        return tasks;
    }
    /**
     * 更新任务
     * @return 更新是否成功
     */
    @Override
    public boolean updateTask(Map<String, Object> map){
        // 更新数据库中的任务
        boolean result = childTaskDAO.updateChildTask(map);
        // 更新redis中的任务
        if (result) {
            TaskChild taskChild = new TaskChild();
            taskChild.setChildId((String) map.get("childId"));
            taskChild.setTaskId((String) map.get("taskId"));
            taskChild.setVolunteerId((String) map.get("volunteerId"));
            taskChild.setFinishAt((LocalDate) map.get("finishAt"));
            taskChild.setAnswer((String) map.get("answer"));
            taskChild.setResponse((String) map.get("response"));
            taskChild.setResponseAt((LocalDate) map.get("responseAt"));
            redisUtil.set("task:" + map.get("childId") + map.get("taskId"), taskChild);
        }
        return result;
    }
    /**
     * 查询任务
     */
    @Override
    public TaskChild selectTask(Map<String, Object> map){
       //从redis中获取任务
        TaskChild taskChild = (TaskChild) redisUtil.get("task:" + map.get("childId") + map.get("taskId"));
        if (taskChild != null) {
            return taskChild;
        }
        //从数据库中获取任务
        taskChild = childTaskDAO.getTaskChildByMap(map);
        //将任务存入redis
        redisUtil.set("task:" + map.get("childId") + map.get("taskId"), taskChild);
        return taskChild;
    }
}
