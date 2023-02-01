package com.atguigu.myzhxy.service.impl;

import com.atguigu.myzhxy.mapper.ClazzMapper;
import com.atguigu.myzhxy.pojo.Clazz;
import com.atguigu.myzhxy.service.ClazzService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("clazzServiceImpl")
@Transactional
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz> implements ClazzService {
    @Override
    public IPage<Clazz> getClazzByOpr(Page<Clazz> page, Clazz clazz) {

        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        if(!StringUtils.isEmpty(gradeName)){
            queryWrapper.like("grade_name",gradeName);
        }
        String name = clazz.getName();
        if(!StringUtils.isEmpty(name)){
            queryWrapper.like("grade_name",name);
        }
        queryWrapper.orderByDesc("id");

        Page<Clazz> page1 = baseMapper.selectPage(page, queryWrapper);
        return page1;
    }

    @Override
    public List<Clazz> getClazzs() {

        List<Clazz> list = baseMapper.selectList(null);
        return list;
    }
}
