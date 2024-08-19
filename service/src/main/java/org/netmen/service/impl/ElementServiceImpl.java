package org.netmen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.ElementMapper;
import org.netmen.dao.po.Element;
import org.netmen.service.ElementService;
import org.springframework.stereotype.Service;

@Service
public class ElementServiceImpl extends ServiceImpl<ElementMapper, Element> implements ElementService {
}