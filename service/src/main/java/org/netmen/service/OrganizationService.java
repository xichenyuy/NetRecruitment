package org.netmen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
//import org.netmen.common.response.Result;
import org.netmen.common.result.Result;
import org.netmen.dao.po.Organization;

import java.util.List;

public interface OrganizationService extends IService<Organization> {

    Organization findByOrganizationName(String name);

    Result deleteOrganization(List<Integer> ids);

    Result changeOrganizationName(Integer organizationId, String newName);

    Result getOrganizationList(Integer currentPage, Integer size);
}
