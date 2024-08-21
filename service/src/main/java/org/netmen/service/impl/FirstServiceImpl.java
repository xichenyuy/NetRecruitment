package org.netmen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.FirstMapper;
import org.netmen.dao.po.First;
import org.netmen.service.FirstService;
import org.springframework.stereotype.Service;

@Service
public class FirstServiceImpl extends ServiceImpl<FirstMapper, First> implements FirstService {

}
