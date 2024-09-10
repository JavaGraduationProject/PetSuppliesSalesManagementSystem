package com.example.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Activity;
import com.example.entity.Orders;
import com.example.entity.Products;
import com.example.entity.Users;
import com.example.result.Result;
import com.example.service.ActivityService;
import com.example.service.OrdersService;
import com.example.service.ProductsService;
import com.example.service.UsersService;
import com.example.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.IconUIResource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.example.utils.ImageBase64Converter.getImgBase64Str;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UsersService usersService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProductsService productsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ActivityService activityService;

    //TODO 管理端首页
    @RequestMapping("index")
    public String index(Model model, String searchYear, String searchMonth) {
        Map<String, Object> map = productsService.getEchartsData(searchYear, searchMonth);
        model.addAttribute("result", map);
        model.addAttribute("searchYear", searchYear);
        model.addAttribute("searchMonth", searchMonth);
        return "index_admin";
    }

    //TODO 转换信息类型
    public static String switchCategory2String(String category) {
        switch (category) {
            case "1":
                category = "狗狗用品";
                break;
            case "2":
                category = "猫咪用品";
                break;
            case "3":
                category = "萌宠周边";
                break;
        }
        return category;
    }

    //TODO 转换信息类型
    public static String switchCategory2Number(String category) {
        switch (category) {
            case "狗狗用品":
                category = "1";
                break;
            case "猫咪用品":
                category = "2";
                break;
            case "萌宠周边":
                category = "3";
                break;
        }
        return category;
    }

    //TODO 业务管理-查询
    @RequestMapping("queryInformation")
    public String queryInformation(Model model, String category, String searchName, @AuthenticationPrincipal UserDetails userDetails) {
        if (null == searchName) {
            searchName = "";
        }
        List<Products> list = productsService.list(new QueryWrapper<Products>().like("product_name", searchName).eq("product_category", category).eq("product_is_delete", false));
        //创建文件夹
        String path = System.getProperty("user.dir");
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        File targetFile = new File(path + "/" + users.getUserId() + "/");
        if (!targetFile.exists()) {
            //不存在就创建一个
            targetFile.mkdir();
        }
        File[] files = targetFile.listFiles();
        for (File f : files) {
            System.out.println(f.getName());
            f.delete();
        }
        List<com.example.entity.vo.Products> result = new LinkedList<>();
        //获取返回前端的数据
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getProductImg() != null) {
                ByteArrayInputStream inputStream = new ByteArrayInputStream(list.get(i).getProductImg());
                //将二进制字节数组 转为文件
                try {
                    Files.copy(inputStream, Paths.get(path + "/" + users.getUserId() + "/" + i + ".jpg"));
                    com.example.entity.vo.Products products = new com.example.entity.vo.Products();
                    products.setProductId(list.get(i).getProductId());
                    products.setProductName(list.get(i).getProductName());
                    products.setProductStyle(list.get(i).getProductStyle());
                    products.setProductAmount(list.get(i).getProductAmount());
                    products.setProductCategory(list.get(i).getProductCategory());
                    products.setProductDescribe(list.get(i).getProductDescribe());
                    products.setProductNumber(list.get(i).getProductNumber());
                    String imgBase64Str = getImgBase64Str(path + "/" + users.getUserId() + "/" + i + ".jpg");
                    products.setUrl("data:image/png;base64," + imgBase64Str);
                    products.setProductCreateTime(list.get(i).getProductCreateTime());
                    products.setProductPrice(list.get(i).getProductPrice());
                    result.add(products);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        category = switchCategory2String(category);
        String categoryNum = switchCategory2Number(category);
        model.addAttribute("result", result);
        model.addAttribute("category", category);
        model.addAttribute("categoryNum", categoryNum);
        model.addAttribute("searchName", searchName);
        return "product_admin";
    }

    //TODO 插入和修改信息
    @PostMapping("insertInformation")
    @ResponseBody
    public Result insertInformation(@Validated Products products, @RequestParam(value = "file") MultipartFile file) {
        //判断文件是否为空
        if (file.isEmpty()) {
            return Result.buildFail("上传图片失败!");
        }
//        String fileName = file.getOriginalFilename();
//        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
//        System.out.println("文件后缀名是："+suffix);
        byte[] bytes = new byte[0];
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        products.setProductImg(bytes);
        if (Objects.isNull(products.getProductId())) {
            long count = productsService.count(new QueryWrapper<Products>().eq("product_name", products.getProductName()));
            if (count >= 1) {
                return Result.buildFail("该名称已存在");
            }
            products.setProductNumber(IdUtil.simpleUUID());
            products.setProductCreateTime(new Date());
            products.setProductIsDelete(false);
        } else {
            products.setProductUpdateTime(new Date());
        }
        productsService.saveOrUpdate(products);
        return Result.buildSuccess();
    }

    //TODO 删除信息
    @RequestMapping("deleteInformation")
    public String deleteInformation(String productId, String category) {
        productsService.deleteProductById(productId);
        category = switchCategory2Number(category);
        return "redirect:/admin/queryInformation?category=" + category;
    }

    //TODO 查询用户
    @RequestMapping("queryUser")
    public String queryUser(Model model, String searchName) {
        if (null == searchName) {
            searchName = "";
        }
        List<Users> result = usersService.list(new QueryWrapper<Users>().like("username", searchName).eq("role", "1").eq("is_delete", false));
        for (Users users : result) {
            users.setPassword("");
        }
        model.addAttribute("result", result);
        model.addAttribute("searchName", searchName);
        return "user";
    }


    //TODO 添加或者修改用户
    @RequestMapping("insertUser")
    @ResponseBody
    public Result insertUser(@Validated Users users, String password_2) {
        System.out.println(users);
        users.setRole("1");
        if (users.getPassword().equals(password_2)) {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            if (null == users.getUserId()) {
                List<Users> list = usersService.list(new QueryWrapper<Users>().eq("username", users.getUsername()));
                if (list.size() > 0) {
                    return Result.buildFail("该用户名已存在");
                }
                users.setCart("");
                users.setCollection("");
                users.setIsDelete(false);
                users.setUserCreateTime(new Date());
            } else {
                users.setUserUpdateTime(new Date());
            }
            usersService.saveOrUpdate(users);
            return Result.buildSuccess("账号编辑成功");
        }
        return Result.buildFail("密码和重复密码输入不一致");
    }

    //TODO 删除用户
    @RequestMapping("deleteUser")
    public String deleteUser(Users users) {
        users.setIsDelete(true);
        usersService.updateById(users);
        return "redirect:/admin/queryUser";
    }

    //TODO 查询管理员
    @RequestMapping("queryAdmin")
    public String queryAdmin(Model model, String searchName) {
        if (null == searchName) {
            searchName = "";
        }
        List<Users> result = usersService.list(new QueryWrapper<Users>().like("username", searchName).eq("role", "2").eq("is_delete", false));
        for (Users users : result) {
            users.setPassword("");
        }
        model.addAttribute("result", result);
        model.addAttribute("searchName", searchName);
        return "admin";
    }

    //TODO 添加或者修改管理员
    @RequestMapping("insertAdmin")
    @ResponseBody
    public Result insertAdmin(@Validated Users users, String password_2) {
        users.setRole("2");
        if (users.getPassword().equals(password_2)) {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            if (null == users.getUserId()) {
                List<Users> list = usersService.list(new QueryWrapper<Users>().eq("username", users.getUsername()));
                if (list.size() > 0) {
                    return Result.buildFail("该用户名已存在");
                }
                users.setCart("");
                users.setCollection("");
                users.setIsDelete(false);
                users.setUserCreateTime(new Date());
            } else {
                users.setUserUpdateTime(new Date());
            }
            usersService.saveOrUpdate(users);
            return Result.buildSuccess("账号编辑成功");
        }
        return Result.buildFail("密码和重复密码输入不一致");
    }

    //TODO 删除管理员
    @RequestMapping("deleteAdmin")
    public String deleteAdmin(Users users) {
        users.setIsDelete(true);
        usersService.updateById(users);
        return "redirect:/admin/queryAdmin";
    }

    //TODO 查看订单
    @GetMapping("queryOrder")
    public String queryOrder(String searchName, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (null == searchName) {
            searchName = "";
        }
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        List<com.example.entity.vo.Orders> list = ordersService.getAllOrder("%" + searchName + "%");
        model.addAttribute("result", list);
        model.addAttribute("searchName", searchName);
        return "order_admin";
    }

    //TODO 删除订单
    @RequestMapping("deleteOrders")
    public String deleteOrders(String orderNumber) {
        ordersService.deleteOrdersByOrderNumber(orderNumber);
        return "redirect:/admin/queryOrder";
    }

    //TODO 发货
    @RequestMapping("sendProduct")
    @ResponseBody
    public String sendProduct(String orderNumber) {
        ordersService.sendProductByOrderNumber(orderNumber);
        return JSON.toJSONString(Result.buildSuccess("发货成功"));
    }

    //TODO 活动页面
    @RequestMapping("activity")
    public String activity(Model model) {
        Activity activity = activityService.getById(1);
        model.addAttribute("result", activity);
        return "activity";
    }

    //TODO 发布活动
    @RequestMapping("addActivity")
    @ResponseBody
    public String addActivity(Model model, String info) {
        Activity activity = activityService.getById(1);
        activity.setInfo(info);
        activity.setIsDelete(false);
        activityService.updateById(activity);
        model.addAttribute("result", activity);
        return JSON.toJSONString(Result.buildSuccess());
    }

    //TODO 发布活动
    @RequestMapping("deleteActivity")
    public String deleteActivity(Model model) {
        Activity activity = activityService.getById(1);
        activity.setIsDelete(true);
        activityService.updateById(activity);
        model.addAttribute("result", activity);
        return "activity";
    }


    //TODO 个人中心
    @RequestMapping("mine")
    public String mine(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Users one = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        Users users = new Users();
        users.setUserId(one.getUserId());
        users.setUsername(one.getUsername());
        users.setAddress(one.getAddress());
        users.setPhone(one.getPhone());
        users.setEmail(one.getEmail());
        model.addAttribute("result", users);
        return "mine_admin";
    }

    //TODO 个人中心修改密码
    @RequestMapping("updateAccount")
    @ResponseBody
    public Result updateAccount(@Validated Users users, String password_2) {
        if (users.getPassword().equals(password_2)) {
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            users.setUserUpdateTime(new Date());
            usersService.updateById(users);
            return Result.buildSuccess("密码修改成功");
        }
        return Result.buildFail("密码和重复密码不一致");
    }

}
