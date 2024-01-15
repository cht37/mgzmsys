package cn.edu.neu.mgzmsys.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TaskChild implements Serializable {
    private String taskId;
  private String childId;
  private String volunteerId;
    private String answer;
    private LocalDate finishAt;
    private String response;
    private LocalDate responseAt;

}
