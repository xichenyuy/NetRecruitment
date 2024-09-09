package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student extends BaseEntity{

    @TableId(type = IdType.AUTO)
    private Integer id;
    // 学号
    private String studentId;
    private String name;
    private Short sex;
    private String grade;
    @TableField(value = "class")
    private Short classId;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer majorId;
    private Short political;
    private String area;
    private String building;
    private String floor;
    private String room;
    private String email;
    private String photo;
    private String introduce;
    private String hobbies;
    private String opinion;
    private String plan;
}
