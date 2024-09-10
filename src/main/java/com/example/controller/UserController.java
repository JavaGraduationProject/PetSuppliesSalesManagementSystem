package com.example.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.Activity;
import com.example.entity.Orders;
import com.example.entity.Products;
import com.example.entity.Users;
import com.example.mapper.ActivityMapper;
import com.example.result.Result;
import com.example.service.ActivityService;
import com.example.service.OrdersService;
import com.example.service.ProductsService;
import com.example.service.UsersService;
import com.example.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.example.controller.AdminController.switchCategory2Number;
import static com.example.controller.AdminController.switchCategory2String;
import static com.example.utils.ImageBase64Converter.getImgBase64Str;

@Controller
@RequestMapping("user")
public class UserController {

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

    //TODO 用户端首页
    @RequestMapping("index")
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Products> list = productsService.list(new QueryWrapper<Products>().eq("product_is_delete", false));
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
                    String orderValuation = ordersService.getOrderValuationByProductId(list.get(i).getProductId());
                    products.setProductId(list.get(i).getProductId());
                    products.setOrderValuation(orderValuation);
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
        Activity activity = activityService.getById(1);
        model.addAttribute("result", result);
        model.addAttribute("activity", activity);
        return "index";
    }

    //TODO 查询信息
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
        return "product";
    }

    //TODO 收藏
    @RequestMapping("collect")
    @ResponseBody
    public Result collect(String productId, @AuthenticationPrincipal UserDetails userDetails) {
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        if (null == users.getCollection()) {
            users.setCollection(productId);
        } else {
            String[] strList = users.getCollection().split(",");
            if (Arrays.asList(strList).contains(productId)) {
                return Result.buildFail("请勿重复收藏");
            } else {
                users.setCollection(users.getCollection() + "," + productId);
            }
        }
        usersService.updateById(users);
        return Result.buildSuccess("已收藏");
    }

    //TODO 添加购物车
    @RequestMapping("addCart")
    @ResponseBody
    public Result addCart(String productId, @AuthenticationPrincipal UserDetails userDetails) {
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        if (null == users.getCart()) {
            users.setCart(productId);
        } else {
            String[] strList = users.getCart().split(",");
            if (Arrays.asList(strList).contains(productId)) {
                return Result.buildFail("请勿重复加入购物车,该商品不适合重复购买");
            } else {
                users.setCart(users.getCart() + "," + productId);
            }
        }
        usersService.updateById(users);
        return Result.buildSuccess("已加入购物车");
    }

    //TODO 查询收藏
    @RequestMapping("queryCollection")
    public String queryCollection(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        String[] strList = users.getCollection().split(",");
        List<Products> list = productsService.list(new QueryWrapper<Products>().in("product_id", strList).eq("product_is_delete", false));
        //创建文件夹
        String path = System.getProperty("user.dir");
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
        model.addAttribute("result", result);
        return "collection";
    }

    //TODO 查看收藏的商品
    @RequestMapping("lookCollection")
    public String lookCollection(String productId) throws UnsupportedEncodingException {
        Products products = productsService.getById(productId);
        switch (products.getProductCategory()) {
            case "1":
                return "redirect:/user/queryInformation?category=1&searchName=" + URLEncoder.encode(products.getProductName(), "utf8");
            case "2":
                return "redirect:/user/queryInformation?category=2&searchName=" + URLEncoder.encode(products.getProductName(), "utf8");
        }
        return JSONUtil.toJsonStr(Result.buildFail("查询失败"));
    }

    //TODO 从收藏中删除
    @RequestMapping("deleteCollection")
    public String deleteCollection(String productId, @AuthenticationPrincipal UserDetails userDetails) {
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        String[] strList = users.getCollection().split(",");
        String str = "";
        for (int i = 0; i < strList.length; i++) {
            if (!productId.equals(strList[i])) {
                str += strList[i] + ",";
            }
        }
        str = str.substring(0, str.length() - 1);
        users.setCollection(str);
        usersService.updateById(users);
        return "redirect:/user/queryCollection";
    }

    //TODO 查看购物车
    @RequestMapping("queryCart")
    public String queryCart(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        String[] strList = users.getCart().split(",");
        List<Products> list = productsService.list(new QueryWrapper<Products>().in("product_id", strList).eq("product_is_delete", false));
        //创建文件夹
        String path = System.getProperty("user.dir");
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
        model.addAttribute("result", result);
        return "cart";
    }

    //TODO 从购物车中删除
    @RequestMapping("deleteCart")
    public String deleteCart(String productId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        usersService.deleteCartByUsernameAndPid(username, productId);
        return "redirect:/user/queryCart";
    }

    //TODO 生成订单
    @RequestMapping("generateOrder")
    @ResponseBody
    public Result generateOrder(String productId, String productAmount, Boolean isGroup, Boolean isCart,
                                @AuthenticationPrincipal UserDetails userDetails) {
        Integer productId_m = StringUtil.changeString(productId);
        Integer productAmount_m = StringUtil.changeString(productAmount);
        if (productAmount_m < 0) {
            return Result.buildFail("您购买的数量不符合要求!");
        }
        Products products = productsService.getById(productId);
        if (products.getProductAmount() < productAmount_m) {
            return Result.buildFail("您购买的商品库存不足!");
        }
        Users one = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        Orders orderResult = ordersService.generateOrder(productId_m, productAmount_m, isGroup, one.getUserId(), isCart);
        Map<String, Object> map = new HashMap<>();
        map.put("orderNumber", orderResult.getOrderNumber());
        return Result.buildSuccess("已生成订单!").setData(map);
    }

    //TODO 付款
    @RequestMapping("payment")
    public String payment(String orderNumber) {
        Orders order = ordersService.paymentByOrderNumber(orderNumber);
        return "redirect:/user/queryOrder";
    }

    //TODO 查看购物车
    @RequestMapping("queryGroup")
    public String queryGroup(String searchName, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (null == searchName) {
            searchName = "";
        }
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        List<com.example.entity.vo.Orders> list = ordersService.getAllGrouping(users.getUserId(), "%" + searchName + "%");
        model.addAttribute("result", list);
        model.addAttribute("searchName", searchName);
        return "group";
    }

    //TODO 拼单
    @RequestMapping("addGenerateOrder")
    @ResponseBody
    public Result addGenerateOrder(String productId, String orderNumber, String productAmount, @AuthenticationPrincipal UserDetails userDetails) {
        Integer productId_m = StringUtil.changeString(productId);
        Integer productAmount_m = StringUtil.changeString(productAmount);
        if (productAmount_m < 0) {
            return Result.buildFail("您购买的数量不符合要求!");
        }
        Products products = productsService.getById(productId);
        if (products.getProductAmount() < productAmount_m) {
            return Result.buildFail("您购买的数量不符合要求!");
        }
        Users one = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        Orders orderResult = ordersService.addGenerateOrder(productId_m, productAmount_m, one.getUserId());
        ordersService.updateOrderByOrderNumber(orderNumber);
        Map<String, Object> map = new HashMap<>();
        map.put("orderNumber", orderResult.getOrderNumber());
        return Result.buildSuccess("已生成订单!").setData(map);
    }

    //TODO 查看订单
    @GetMapping("queryOrder")
    public String queryOrder(String searchName, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (null == searchName) {
            searchName = "";
        }
        Users users = usersService.getOne(new QueryWrapper<Users>().eq("username", userDetails.getUsername()));
        List<com.example.entity.vo.Orders> list = ordersService.getAllOrderByUserId(users.getUserId(), "%" + searchName + "%");
        model.addAttribute("result", list);
        model.addAttribute("searchName", searchName);
        return "order";
    }

    //TODO 评价
    @RequestMapping("addOrderValuation")
    @ResponseBody
    public Result addOrderValuation(String orderNumber, String orderValuation) {
        ordersService.valuationOrderByOrderNumber(orderNumber, orderValuation);
        return Result.buildSuccess("已评价");
    }

    //TODO 收货
    @RequestMapping("receiptProduct")
    public String receiptProduct(String orderNumber) {
        ordersService.receiptProductByOrderNumber(orderNumber);
        return "redirect:/user/queryOrder";
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
        return "mine";
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

    //TODO 清空购物车
    @RequestMapping("clearCart")
    public String clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        usersService.clearCart(userDetails.getUsername());
        return "redirect:/user/queryCart";
    }
}
