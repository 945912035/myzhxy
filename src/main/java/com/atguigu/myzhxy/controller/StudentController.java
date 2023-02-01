package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;



    @DeleteMapping("/delStudentById")
    public Result delStudentById(
            @RequestBody List<Integer> integerList
    ){
        studentService.removeBatchByIds(integerList);
        return Result.ok();
    }

    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @RequestBody Student student
    ){
        Integer id = student.getId();
        if(null==id || 0== id){
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }


    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            Student student
    ){
        Page<Student> page = new Page<>(pageNo,pageSize);
        IPage<Student> page1 = studentService.getStudentByOpr(page, student);
        return Result.ok(page1);
    }
}
