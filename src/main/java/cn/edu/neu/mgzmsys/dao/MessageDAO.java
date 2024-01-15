package cn.edu.neu.mgzmsys.dao;

import cn.edu.neu.mgzmsys.entity.Message;
import cn.edu.neu.mgzmsys.mapper.MessageMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class MessageDAO {
    @Resource
    private MessageMapper messageMapper;
    public List<Message> searchMessage(String conversationId,String senderId) {
       LambdaQueryWrapper<Message> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //按照message_time时间排序
        lambdaQueryWrapper.and(i->i.and(e->e.eq(Message::getMessageStatus,2)).or(e->e.eq(Message::getPosterId,senderId).eq(Message::getMessageStatus,1))).orderByAsc(Message::getMessageTime).eq(Message::getConversationId,conversationId);
        return messageMapper.selectList(lambdaQueryWrapper);
    }
    public boolean insertMessage(Message message) {
       return messageMapper.insert(message) == 1;
    }
}
