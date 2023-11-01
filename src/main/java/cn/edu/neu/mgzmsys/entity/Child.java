package cn.edu.neu.mgzmsys.entity;

import java.io.Serializable;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 儿童
 * </p>
 *
 * @author team15
 * @since 2023-11-01
 */
@Getter
@Setter
  public class Child implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 儿童唯一标识的id
     */
        private String id;

      /**
     * 账号
     */
      private String username;

      /**
     * 账户密码
     */
      private String password;

      /**
     * 儿童姓名
     */
      private String childName;

      /**
     * 儿童性别
     */
      private Integer gender;

      /**
     * 儿童生日
     */
      private LocalDate birthday;

      /**
     * 儿童住址
     */
      private String address;

      /**
     * 电话
     */
      private String phone;

      /**
     * 儿童爱好
     */
      private String hobby;

      /**
     * 儿童描述
     */
      private String description;


}
