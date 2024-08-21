package org.netmen.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.netmen.dao.mapper.SecondMapper;
import org.netmen.dao.po.Second;
import org.netmen.service.SecondService;
import org.springframework.stereotype.Service;

@Service
public class SecondServiceImpl extends ServiceImpl<SecondMapper, Second> implements SecondService{
}
