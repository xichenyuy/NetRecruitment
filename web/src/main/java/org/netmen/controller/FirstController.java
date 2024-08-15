package org.netmen.controller;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.netmen.common.result.Result;
import org.netmen.dao.po.First;
import org.netmen.service.FirstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "first")
@RequestMapping("/first")
public class FirstController {

    @Autowired
    private FirstService firstService;

    /**
     * 分页查询
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GetMapping
    public Result getFirst(@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "10") Integer pageSize) {
        //1. 创建分页参数
        //1.1 分页条件
        Page<First> page = Page.of(pageNo, pageSize);
        //1.2 排序条件
        page.addOrder(new OrderItem());
        //2. 分页查询
        Page<First> p = firstService.page(page);
        //3. 解析
        log.info("总条数：{}，总页数：{}", page.getTotal(), page.getPages());
        return Result.success().data(p);
    }


    /**
     * 根据id修改
     * @param first
     * @return
     */
    @PutMapping
    public Result<String> updateFirst(@RequestBody First first) {
        firstService.updateById(first);
        return Result.success();
    }

    /**
     * 根据id删除
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public Result<String> deleteFirst(@PathVariable("ids") List<Integer> ids) {
        firstService.removeBatchByIds(ids);
        return Result.success();
    }
}
