package org.netmen.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Second;
import org.netmen.service.SecondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "second")
@RequestMapping("/second")
public class SecondController {

    @Autowired
    private SecondService secondService;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping
    public Result getSecond(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        //1. 创建分页参数
        //1.1 分页条件
        Page<Second> page = Page.of(pageNo, pageSize);
        //1.2 排序条件
        page.addOrder(new OrderItem());
        //2. 分页查询
        Page<Second> p = secondService.page(page);
        //3. 解析
        log.info("总条数：{}，总页数：{}", page.getTotal(), page.getPages());
        return Result.success().data(p);
    }


    /**
     * 根据id修改
     * @param second
     * @return
     */
    @PutMapping
    public Result<String> updateSecond(@RequestBody Second second) {
        secondService.updateById(second);
        return Result.success();
    }

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public Result<String> deleteSecond(@PathVariable("ids") List<Integer> ids) {
        secondService.removeBatchByIds(ids);
        return Result.success();
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable("id") Integer id) {
        return Result.success().data(secondService.getById(id));
    }
}
