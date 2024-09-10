package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Activity;
import com.example.entity.Users;
import com.example.exception.CustomException;
import com.example.mapper.ActivityMapper;
import com.example.result.Result;
import com.example.service.ActivityService;
import com.example.service.UsersService;
import com.example.utils.ReplaceBackground;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;


@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersService usersService;
    @Autowired
    private ActivityService activityService;

    //TODO 登录页
    @RequestMapping("/login_page")
    public String login(Model model) {
        List<Users> list = usersService.list(new QueryWrapper<Users>().eq("username", "admin"));
        List<Activity> list1 = activityService.list(new QueryWrapper<Activity>().eq("id", 1));
        if (list1.size() == 0){
            Activity activity = new Activity();
            activity.setId(1);
            activity.setIsDelete(false);
            activityService.save(activity);
        }
        if (!(list.size() > 0)) {
            Users users = new Users();
            users.setUsername("admin");
            users.setPassword(passwordEncoder.encode("123456"));
            users.setAddress("");
            users.setIsDelete(false);
            users.setPhone("12312341234");
            users.setEmail("153131@qq.com");
            users.setCollection("");
            users.setCart("");
            users.setUserCreateTime(new Date());
            users.setRole("2");
            users.setIsDelete(false);
            usersService.save(users);
        }
        String imgBase64Str = ReplaceBackground.replaceImg();
        model.addAttribute("imgBase64Str", imgBase64Str);
        return "login_page";
    }

    //TODO 登录页
    @RequestMapping("/")
    public String nullPath(Model model) {
        List<Users> list = usersService.list(new QueryWrapper<Users>().eq("username", "admin"));
        List<Activity> list1 = activityService.list(new QueryWrapper<Activity>().eq("id", 1));
        if (list1.size() == 0){
            Activity activity = new Activity();
            activity.setId(1);
            activity.setIsDelete(false);
            activityService.save(activity);
        }
        if (!(list.size() > 0)) {
            Users users = new Users();
            users.setUsername("admin");
            users.setPassword(passwordEncoder.encode("123456"));
            users.setAddress("");
            users.setIsDelete(false);
            users.setPhone("12312341234");
            users.setEmail("153131@qq.com");
            users.setCollection("");
            users.setCart("");
            users.setUserCreateTime(new Date());
            users.setRole("2");
            users.setIsDelete(false);
            usersService.save(users);
        }
        String imgBase64Str = ReplaceBackground.replaceImg();
        model.addAttribute("imgBase64Str", imgBase64Str);
        return "login_page";
    }

    //TODO 登录系统后根据用户角色导航到管理端或者用户端
    @RequestMapping("/login/login")
    public String loginLogin(HttpServletRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        Users one = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        if ("1".equals(one.getRole())) {
            return "redirect:/user/index";
        } else if ("2".equals(one.getRole())) {
            return "redirect:/admin/index";
        } else {
            return "redirect:/logout";
        }
    }

    //TODO 注册用户
    @PostMapping("/login/registration")
    @ResponseBody
    public String registration(@Validated Users users, String password_2) throws CustomException {
        System.out.println(users);
        users.setRole("1");
        users.setCart("");
        users.setCollection("");
        users.setIsDelete(false);
        if (users.getPassword().equals(password_2)) {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            List<Users> list = usersService.list(new QueryWrapper<Users>().eq("username", users.getUsername()));
            if (list.size() > 0) {
                return JSON.toJSONString(Result.buildFail("该用户名已存在"));
            }
            users.setUserCreateTime(new Date());
            usersService.save(users);
            return JSON.toJSONString(Result.buildSuccess("注册成功"));
        }
        return JSON.toJSONString(Result.buildFail("密码和确认密码不一致"));
    }

}
