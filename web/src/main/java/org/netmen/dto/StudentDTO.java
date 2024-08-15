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
public class StudentDTO {
    private String name;
    private String grade;
    private Long majorId;
    @TableField(value = "class")
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
