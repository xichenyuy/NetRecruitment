package org.netmen.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@TableName("student")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @TableId(value = "student_id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String grade;
    private Long majorId;
    private Short classId;
    private String area;
    private String door;
    private String storey;
    private String email;
    private Short sex;
    private Short political;
    private Integer firstId;
    private Integer secondId;
    private Short adjust;
    private String photo;
    private String introduce;
    private String hobbies;
    private String opinion;
    private String plan;
}
