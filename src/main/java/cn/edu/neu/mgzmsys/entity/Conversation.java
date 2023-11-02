package cn.edu.neu.mgzmsys.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author team15
 * @since 2023-11-02
 */
@Data
public class Conversation implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会话id
     */
    private String conversationID;

    /**
     * 参与者1
     */
    private String participantOneID;

    /**
     * 参与者2
     */
    private String participantTwoID;


}
