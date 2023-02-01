package com.atguigu.myzhxy.controller;


import com.atguigu.myzhxy.pojo.Grade;
import com.atguigu.myzhxy.service.GradeService;

import com.atguigu.myzhxy.util.JwtHelper;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping("/getGrades")
    public Result getGrades(){

        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }


    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @RequestBody List<Integer> grade
    ){
        gradeService.removeBatchByIds(grade);
        return Result.ok();
    }


    @PostMapping("/saveOrUpdateGrade")
     public Result saveOrUpdateGrade(@RequestBody Grade grade){

        gradeService.saveOrUpdate(grade);

        return Result.ok();

     }

     @GetMapping("/getGrades/{pageNo}/{pageSize}")
     public Result getGrades(@PathVariable("pageNo") Integer pageNo,
                             @PathVariable("pageSize") Integer pageSize,
                             String gradeName){
         Page<Grade> page = new Page<>(pageNo,pageSize);
         IPage<Grade> gradeByOpr = gradeService.getGradeByOpr(page, gradeName);

         return Result.ok(gradeByOpr);
     }


}
