package com.atguigu.myzhxy.controller;

import com.atguigu.myzhxy.pojo.Admin;
import com.atguigu.myzhxy.pojo.LoginForm;
import com.atguigu.myzhxy.pojo.Student;
import com.atguigu.myzhxy.pojo.Teacher;
import com.atguigu.myzhxy.service.AdminService;
import com.atguigu.myzhxy.service.StudentService;
import com.atguigu.myzhxy.service.TeacherService;
import com.atguigu.myzhxy.util.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;

    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @RequestHeader("token") String token,
            @PathVariable("oldPwd") String oldPwd,
            @PathVariable("newPwd") String newPwd
    ){
        if (JwtHelper.isExpiration(token)) {
            return Result.fail().message("token is error or lose");
        }
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        switch (userType){
            case 1:
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id",userId.intValue());
                queryWrapper1.eq("password",oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);

                if(admin != null){
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                }else {
                    return Result.fail().message("oldPwd is error");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id",userId.intValue());
                queryWrapper2.eq("password",oldPwd);
                Student student = studentService.getOne(queryWrapper2);

                if(student != null){
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                }else {
                    return Result.fail().message("oldPwd is error");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id",userId.intValue());
                queryWrapper3.eq("password",oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);

                if(teacher != null){
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                }else {
                    return Result.fail().message("oldPwd is error");
                }
                break;
        }
        return Result.ok();

    }

    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
            ){
        String uuid = UUID.randomUUID().toString().replace("-","").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String newSubstring = uuid.concat(originalFilename.substring(index));

        String Path = "E:/MyFirstOne/myzhxy/target/classes/public/upload/".concat(newSubstring);
        try {
            multipartFile.transferTo(new File(Path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = "upload/".concat(newSubstring);

        return Result.ok(path);
    }

    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token){
        //token有效验证
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //从token中解析出用户id和用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String,Object> map = new LinkedHashMap<>();
        switch (userType){
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType",1);
                map.put("user",admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType",2);
                map.put("user",student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType",3);
                map.put("user",teacher);
                break;
        }

        return Result.ok(map);
    }


    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm,HttpServletRequest request){
        //验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if("".equals(sessionVerifiCode) || null == sessionVerifiCode){
            return Result.fail().message("验证码失效，请刷新后重试！");
        }
        if(!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)){
            return Result.fail().message("验证码有误，请重试！");
        }
        //从session域中移除现有验证码
        session.removeAttribute("verifiCode");
        //用户类型进行校验表格中数据


        //准备一个map集合用于用户存放响应的数据
        Map<String,Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()){
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if(null!=admin){
                        //用户的id和类型转化成一个密文以token的名称向客户端返回
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if(null!=student){
                        //用户的id和类型转化成一个密文以token的名称向客户端返回
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if(null!=teacher){
                        //用户的id和类型转化成一个密文以token的名称向客户端返回
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token",token);
                    }else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户！");


    }


    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response){
        //获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        //获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        //将验证码放入session域，为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode",verifiCode);
        //将验证码响应给浏览器

        try {
            ImageIO.write(verifiCodeImage,"JPEG",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
