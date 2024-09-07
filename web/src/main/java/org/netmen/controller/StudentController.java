package org.netmen.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.netmen.common.result.Result;
import org.netmen.dao.po.InterviewStatus;
import org.netmen.dao.po.Student;
import org.netmen.dto.StudentDTO;
import org.netmen.dto.StudentDTOId;
import org.netmen.service.InterviewStatusService;
import org.netmen.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "student")
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private InterviewStatusService interviewStatusService;


    /**
     * 添加学生
     * @param studentDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "添加学生接口", description = "添加学生个人信息")
    public Result addStudent(@RequestBody StudentDTO studentDTO) {
        try {
            Student student1 = studentService.getByStudentId(studentDTO.getStudentId());
            if (student1 != null){
                return Result.error().message("该学生已存在,请检查学号是否有误或联系管理员");
            }
            Student student = new Student();
            InterviewStatus interviewStatus = new InterviewStatus();
            BeanUtils.copyProperties(studentDTO, student);
            BeanUtils.copyProperties(studentDTO, interviewStatus);
            // 1.添加学生
            studentService.saveStudent(student, interviewStatus);
            return Result.success().message("添加成功");
        }catch (Exception e){
            return Result.error().message("添加失败");
        }

    }

    /**
     * 根据id批量删除数据
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    @Operation(summary = "物理删除接口", description = "在数据库中彻底删除")
    public Result deleteStudent(@PathVariable("ids") List<Integer> ids) {
        try {
            studentService.deleteStudentByIds(ids);
            return Result.success().message("删除成功");
        }catch (Exception e){
            return Result.error().message("删除失败");
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "逻辑删除接口", description = "修改属性deleted")
    public Result updateDeleted(@PathVariable("id") Integer id){
        try {
            studentService.updateDeleted(id);
            interviewStatusService.updateDeleted(id);
            return Result.success().message("删除成功");
        }catch (Exception e){
            return Result.error().message("删除失败");
        }

    }

    /**
     * 根据学生id修改学生信息
     * @param studentDTOId
     * @return
     */
    @PutMapping
    @Operation(summary = "修改学生接口", description = "修改学生个人信息")
    public Result updateStudent(@RequestBody StudentDTOId studentDTOId) {
        try {
            Student student = new Student();
            InterviewStatus interviewStatus = new InterviewStatus();
            BeanUtils.copyProperties(studentDTOId, student);
            BeanUtils.copyProperties(studentDTOId, interviewStatus);
            log.info("student表id：{}", student.getId());
            studentService.updateById(student);
            log.info("interviewStatus表id：{}", interviewStatus.getId());
            interviewStatusService.updateById(interviewStatus);
            return Result.success().message("修改成功");
        }catch (Exception e){
            return Result.error().message("修改失败");
        }

    }

    /**
     * 分页查询
     * @return
     */
    @GetMapping
    @Operation(summary = "分页查询学生接口", description = "分页查询学生个人信息接口")

    public Result getStudents(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            //1. 创建分页参数
            //1.1 分页条件
            Page<Student> page = Page.of(pageNo, pageSize);
            //1.2 排序条件
            page.addOrder(new OrderItem());
            //2. 分页查询
            Page<Student> p = studentService.page(page);
            //3. 解析
            log.info("总条数：{}，总页数：{}", page.getTotal(), page.getPages());
            return Result.success().data(p);
        }catch (Exception e){
            return Result.error().message("查询失败");
        }

    }

    /**
     * 根据名字进行模糊查询，并实现分页展示
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/search")
    @Operation(summary = "根据名字模糊查询学生接口", description = "根据名字模糊分页查询学生个人信息")
    public Result getStudentByName(@RequestParam String name,
                                   @RequestParam Integer pageNo,
                                   @RequestParam Integer pageSize) {
        try {
            List<Student> result = studentService.findStudentsByName(name, pageNo, pageSize);
            return Result.success().data(result);
        }catch (Exception e){
            return Result.error().message("查询失败");
        }

    }
}
