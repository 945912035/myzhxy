package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.Grade;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.service.GradeService;
import com.atguigu.myzhxy.util.MD5;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private GradeService gradeService;
    @Autowired
    private AdminService adminService;


    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            String adminName
    ){
        Page<Admin> page = new Page<>(pageNo,pageSize);
        IPage<Admin>  iPage= adminService.getAdminByOpr(page,adminName);

        return Result.ok(iPage);
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
            @RequestBody List<Integer> list
            ){
        adminService.removeBatchByIds(list);
        return Result.ok();
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(
            @RequestBody Admin admin
    ){
        Integer id = admin.getId();
        if (id==null||0==id){
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            String admin
    ) {
        //chaXun分页 带条件查询
        Page<Grade> page = new Page<>(pageNo, pageSize);

        //通过服务层进行查询
        IPage<Grade> iPage = gradeService.getGradeByOpr(page, admin);

        //封装Result对象并返回
        return Result.ok(iPage);
    }
}
