package cn.edu.neu.mgzmsys.service.impl;

import cn.edu.neu.mgzmsys.common.utils.RedisUtil;
import cn.edu.neu.mgzmsys.component.RabbitMQQueueManager;
import cn.edu.neu.mgzmsys.dao.ConversationDAO;
import cn.edu.neu.mgzmsys.entity.Conversation;
import cn.edu.neu.mgzmsys.mapper.ConversationMapper;
import cn.edu.neu.mgzmsys.service.IConversationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
@Service
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation> implements IConversationService {
    @Resource
    ConversationDAO conversationDAO;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RabbitMQQueueManager rabbitMQQueueManager;

    /**
     * 建立会话
     *
     * @return 是否建立成功
     */
    @Override
    public boolean setupConversation(Map<String, String> map) {
        if ( map.get("participation1") == null || map.get("participation2") == null ) {
            return false;
        }else if ( map.get("participation1").equals(map.get("participation2")) ) {
            return false;
        }else if ( conversationDAO.searchByTwoParticipantIds(map.get("participation1"), map.get("participation2")) != null ) {
            return false;
        }else {
            Conversation conversation = conversationDAO.setupConversation(map);
            // 在RabbitMQ中创建以conversationId为name的队列
            rabbitMQQueueManager.createQueueIfNotExists(conversation.getConversationId());
            return true;
        }
    }

    /**
     * 根据两个参与者id获取会话
     */
    @Override
    public Conversation getByTwoParticipantIds(String participantId1, String participantId2) {
        if ( participantId1 == null || participantId2 == null ) {
            return null;
        } else {
            return conversationDAO.searchByTwoParticipantIds(participantId1, participantId2);
        }
    }
    /**
     * 根据会话id获取会话list
     */
    @Override
    public List<Conversation> getByParticipantId(String participantId) {
         return conversationDAO.getConversationListByParticipantId(participantId);
    }
}
