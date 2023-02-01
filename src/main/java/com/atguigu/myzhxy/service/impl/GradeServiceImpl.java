package com.atguigu.myzhxy.service.impl;

import com.atguigu.myzhxy.mapper.GradeMapper;
import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.Grade;
import com.atguigu.myzhxy.service.GradeService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("gradeServiceImpl")
@Transactional
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade> implements GradeService {
    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {

        QueryWrapper<Grade> queryWrapper = new QueryWrapper();
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("name", gradeName);
        }
        queryWrapper.orderByAsc("id");

        Page<Grade> page1 = baseMapper.selectPage(page, queryWrapper);

        return page1;
    }

    @Override
    public List<Grade> getGrades() {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper(null);
        List<Grade> grades = baseMapper.selectList(queryWrapper);

        return grades;
    }

}
