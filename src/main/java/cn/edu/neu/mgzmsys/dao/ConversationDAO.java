package cn.edu.neu.mgzmsys.dao;

import cn.edu.neu.mgzmsys.entity.Conversation;
import cn.edu.neu.mgzmsys.mapper.ConversationMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class ConversationDAO {
    @Resource
    private ConversationMapper conversationMapper;

    public Conversation setupConversation(Map<String, String> map) {
            Conversation conversation=new Conversation();
            conversation.setParticipantOneId(map.get("participation1"));
            conversation.setParticipantTwoId(map.get("participation2"));
            conversationMapper.insert(conversation);
            return conversation;
        }
    public Conversation searchByTwoParticipantIds(String participantId1, String participantId2) {
        return conversationMapper.searchByTwoParticipantIds(participantId1, participantId2);
    }
    public List<Conversation> getConversationListByParticipantId(String participantId) {
        LambdaQueryWrapper<Conversation> lambdaQueryWrapper = new LambdaQueryWrapper<>();
       lambdaQueryWrapper.and(i -> i.eq(Conversation::getParticipantOneId, participantId).or().eq(Conversation::getParticipantTwoId, participantId));
        return conversationMapper.selectList(lambdaQueryWrapper);
    }
}
