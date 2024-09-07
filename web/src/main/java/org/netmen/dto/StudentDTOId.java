package org.netmen.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTOId {
    private Integer id;
    private String name;
    private String grade;
    private String studentId;
    private Integer majorId;
    @TableField(value = "class")
    private Short classId;
    private String area;
    private String room;
    private String building;
    private String floor;
    private String email;
    private Short sex;
    private Short political;
    private Integer firstDepartmentId;
    private Integer secondDepartmentId;
    private Short adjust;
    private String photo;
    private String introduce;
    private String hobbies;
    private String opinion;
    private String plan;
}
