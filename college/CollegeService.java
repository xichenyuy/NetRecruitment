package org.netmen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.netmen.common.response.Result;
import org.netmen.dao.po.College;

import java.util.List;

public interface CollegeService extends IService<College> {

    College findByCollegeName(String name);

    Result deleteCollege(List<Integer> ids);

    Result changeCollegeName(Integer organizationId, String newName);

    Result getCollegeList(Integer currentPage, Integer size);
}
