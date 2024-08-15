package org.netmen.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.netmen.common.result.Result;
import org.netmen.dao.po.First;
import org.netmen.dao.po.Student;
import org.netmen.dto.StudentDTO;
import org.netmen.service.FirstService;
import org.netmen.service.StudentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
     * @param studentDTO
     * @return
     */
    @PostMapping
    public Result addStudent(@RequestBody StudentDTO studentDTO) {
        Student student = new Student();
        BeanUtils.copyProperties(studentDTO, student);
        studentService.saveStudent(student);
//        // 1.添加学生
//        boolean save = studentService.save(student);
//        // 注意：因为student表的id设置为自增，在执行save后才给id赋值
//        log.info("student_id:{}", student.getId());
//        first.setFirstId(student.getId());
//        log.info("first_id:{}", first.getFirstId());
//        // 2.添加学生第一志愿信息
//        boolean save1 = firstService.save(first);

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
    public Result<String> updateStudent(@RequestBody Student student) {
        studentService.updateById(student);
        return Result.success();
    }

    /**
     * 分页查询
     * @return
     */
    @GetMapping
    public Result getStudents(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
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
    }

    /**
     * 根据名字进行模糊查询，并实现分页展示
     * @param name
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping("/search")
    public Result getStudentByName(@RequestParam String name,
                                   @RequestParam Integer pageNo,
                                   @RequestParam Integer pageSize) {
        List<Student> result = studentService.findStudentsByName(name, pageNo, pageSize);
        return Result.success().data(result);
    }
}
