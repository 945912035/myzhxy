package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(
            @RequestBody List<Integer> list
            ){
        teacherService.removeBatchByIds(list);

        return Result.ok();
    }

    //saveOrUpdateTeacher

    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(
            @RequestBody Teacher teacher
    ){
        //如果数据库没有这个东西。
        if(teacher.getId()==null|teacher.getId()==0){
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            Teacher teacher
    ){
        Page<Teacher> page = new Page<>(pageNo,pageSize);
        IPage<Teacher> iPage = teacherService.getTeacherByOpr(page,teacher);
        return Result.ok(iPage);
    }

}
