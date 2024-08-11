package org.netmen.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.netmen.common.response.Result;
import org.netmen.dao.po.Student;
import org.netmen.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "student")
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 添加学生
     * @param student
     * @return
     */
    @PostMapping
    public Result<String> addStudent(Student student) {
        studentService.save(student);
        return Result.success();
    }

    /**
     * 根据id批量删除数据
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public Result<String> deleteStudent(@PathVariable("ids") List<Integer> ids) {
        studentService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 根据学生id修改学生信息
     * @param student
     * @return
     */
    @PutMapping
    public Result<String> updateStudent(Student student) {
        studentService.updateById(student);
        return Result.success();
    }

    /**
     * 分页查询
     * @return
     */
    @GetMapping
    public Result<Page<Student>> getStudents(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        //1. 创建分页参数
        //1.1 分页条件
        Page<Student> page = Page.of(pageNo, pageSize);
        //1.2 排序条件
        page.addOrder(new OrderItem());
        //2. 分页查询
        Page<Student> p = studentService.page(page);
        //3. 解析
        log.info("总条数：{}，总页数：{}", page.getTotal(), page.getPages());
        return Result.success(page);
    }
}
