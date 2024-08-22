package org.netmen.dao.po;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * PO 与数据库一一对应
 */
@Data
@TableName("user")
public class User extends BaseEntity{
    @Pattern(regexp = "^\\S{5,16}$")
    private String username;
    @JSONField(serialize = false) //让springmvc将当前对象转换成json时, 忽略password, 防止user信息连同密码一起返回
    @Pattern(regexp = "^\\S{5,16}$")
    private String password;
    @NotEmpty
    @Pattern(regexp = "^\\S{1,16}$")
    private String nickname;
    @NotEmpty
    @Email
    private String email;
    private String userPic;
    private Boolean superuser;
    private Boolean disabled;
}
