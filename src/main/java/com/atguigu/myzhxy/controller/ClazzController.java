package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Clazz;
import com.atguigu.myzhxy.pojo.Grade;
import com.atguigu.myzhxy.service.ClazzService;
import com.atguigu.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;

    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> list = clazzService.getClazzs();
        return Result.ok(list);
    }

    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
            @RequestBody List<Integer> list
    ){
        clazzService.removeBatchByIds(list);


        return Result.ok();
    }


    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(
            @PathVariable("pageNo") Integer pageNo,
            @PathVariable("pageSize") Integer pageSize,
            Clazz clazz
            ){

        Page<Clazz> page = new Page<>(pageNo,pageSize);
        IPage<Clazz> iPage = clazzService.getClazzByOpr(page,clazz);

        return Result.ok(iPage);
    }
}
