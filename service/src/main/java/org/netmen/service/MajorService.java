package org.netmen.service;

import com.baomidou.mybatisplus.extension.service.IService;
//import org.netmen.common.response.Result;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Major;

import java.util.List;

public interface MajorService extends IService<Major> {

    Major findMajorByName(String name);

    Result deleteMajors(List<Integer> ids);

    Result updataMajorName(Integer majorId, String name,Integer newCollegeId);

    Result getMajorList(Integer collegeId, Integer pageNum, Integer pageSize);
}
