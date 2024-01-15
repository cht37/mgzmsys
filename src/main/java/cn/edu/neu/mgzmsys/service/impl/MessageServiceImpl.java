package cn.edu.neu.mgzmsys.service.impl;

import cn.edu.neu.mgzmsys.common.utils.RedisUtil;
import cn.edu.neu.mgzmsys.component.MQConsumer;
import cn.edu.neu.mgzmsys.component.MQProducer;
import cn.edu.neu.mgzmsys.dao.MessageDAO;
import cn.edu.neu.mgzmsys.entity.Message;
import cn.edu.neu.mgzmsys.mapper.MessageMapper;
import cn.edu.neu.mgzmsys.service.IMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Queue;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    @Resource
    MessageDAO messageDAO;
    @Resource
    RedisUtil redisUtil;
    @Resource
    MQProducer mqProducer;

    @Resource
    MQConsumer mqConsumer;

    /**
     * 根据会话id获取消息,按照时间排序
     *
     * @return 消息列表
     */
    @Override
    public List<Message> selectMessage(String connectionId, String senderId) {
        //从redis中获取消息
        List<Message> messages = (List<Message>) redisUtil.get("message:" + connectionId + senderId);
        if (messages != null) {
            return messages;
        }
        //从数据库中获取消息
        messages = messageDAO.searchMessage(connectionId, senderId);
        //将消息存入redis
        redisUtil.set("message:" + connectionId + senderId, messages);
        return messages;
    }

    /**
     * 消息队列存储离线发送的消息
     */
    @Override
    public void handleSentMessage(Message message) {
        mqProducer.sendToChatQueue(message);
    }

    /**
     * 消息队列获取离线发送的消息
     */
    @Override
    public Queue<Message> getSentMessages(String queueName) {
        return mqConsumer.getMessagesFromQueue(queueName);
    }

    /**
     * 保存在线发送的消息
     */
    @Override
    public boolean saveMessage(Message message) {

        return messageDAO.insertMessage(message);
    }
}
