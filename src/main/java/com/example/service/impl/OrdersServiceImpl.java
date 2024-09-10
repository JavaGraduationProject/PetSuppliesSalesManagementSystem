package com.example.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.OrderProductUser;
import com.example.entity.Orders;
import com.example.entity.Products;
import com.example.exception.CustomException;
import com.example.mapper.OrderProductUserMapper;
import com.example.mapper.OrdersMapper;
import com.example.mapper.ProductsMapper;
import com.example.service.OrdersService;
import com.example.service.ProductsService;
import com.example.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * (Orders)表服务实现类
 *
 * @author makejava
 * @since 2022-03-24 16:55:10
 */
@Service("ordersService")
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderProductUserMapper orderProductUserMapper;
    @Autowired
    private ProductsMapper productsMapper;
    @Autowired
    private UsersService usersService;

    @Override
    public Orders generateOrder(Integer productId, Integer productAmount_m, Boolean isGroup, Integer userId, Boolean isCart) {
        Orders order = new Orders();
        String orderNumber = IdUtil.simpleUUID();
        order.setOrderNumber(orderNumber);
        order.setOrderAmount(productAmount_m);
        order.setOrderIsPay(false);
        order.setOrderIsReceipt("1");
        order.setOrderIsGroup(false);
        order.setOrderIsGroupEnd(true);
        order.setOrderIsEvaluate(false);
        order.setOrderCreateTime(new Date());
        order.setOrderIsGroup(isGroup);
        order.setOrderIsGroupEnd(false);
        order.setOrderIsDelete(false);
        int insert = ordersMapper.insert(order);
        if (insert <= 0) {
            throw new CustomException("生成订单失败");
        }
        Orders selectOne = ordersMapper.selectOne(new QueryWrapper<Orders>().eq("order_number", orderNumber));
        OrderProductUser orderProductUser = new OrderProductUser();
        orderProductUser.setOrderId(selectOne.getOrderId());
        orderProductUser.setProductId(productId);
        orderProductUser.setUserId(userId);
        int insert1 = orderProductUserMapper.insert(orderProductUser);
        if (insert1 <= 0) {
            throw new CustomException("生成订单失败");
        }
        //删除购物车
        if (isCart !=null && isCart) {
            usersService.deleteCartByUserIdAndPid(userId.toString(), productId.toString());
        }
        return selectOne;
    }

    @Override
    public Orders paymentByOrderNumber(String orderNumber) {
        ordersMapper.paymentByOrderNumber(orderNumber);
        Orders one = ordersMapper.selectOne(new QueryWrapper<Orders>().eq("order_number", orderNumber));
        OrderProductUser orderProductUser = orderProductUserMapper.selectOne(new QueryWrapper<OrderProductUser>().eq("order_id", one.getOrderId()));
        Products products = productsMapper.selectById(orderProductUser.getProductId());
        products.setProductAmount(products.getProductAmount() - one.getOrderAmount());
        productsMapper.updateById(products);
        return one;
    }

    @Override
    public List<com.example.entity.vo.Orders> getAllGrouping(Integer userId, String searchCon) {
        return ordersMapper.getAllGrouping(userId, searchCon);
    }

    @Override
    public Orders addGenerateOrder(Integer productId, Integer productAmount, Integer userId) {
        Orders order = new Orders();
        String orderNumber = IdUtil.simpleUUID();
        order.setOrderNumber(orderNumber);
        order.setOrderAmount(productAmount);
        order.setOrderIsPay(false);
        order.setOrderIsReceipt("1");
        order.setOrderIsGroup(false);
        order.setOrderIsGroupEnd(false);
        order.setOrderIsEvaluate(false);
        order.setOrderCreateTime(new Date());
        order.setOrderIsGroup(true);
        order.setOrderIsGroupEnd(true);
        order.setOrderIsDelete(false);
        int insert = ordersMapper.insert(order);
        if (insert <= 0) {
            throw new CustomException("生成订单失败");
        }
        Orders selectOne = ordersMapper.selectOne(new QueryWrapper<Orders>().eq("order_number", orderNumber));
        OrderProductUser orderProductUser = new OrderProductUser();
        orderProductUser.setOrderId(selectOne.getOrderId());
        orderProductUser.setProductId(productId);
        orderProductUser.setUserId(userId);
        int insert1 = orderProductUserMapper.insert(orderProductUser);
        if (insert1 <= 0) {
            throw new CustomException("生成订单失败");
        }
        return selectOne;
    }

    @Override
    public Boolean updateOrderByOrderNumber(String orderNumber) {
        return ordersMapper.updateOrderByOrderNumber(orderNumber);
    }

    @Override
    public List<com.example.entity.vo.Orders> getAllOrderByUserId(Integer userId, String searchCon) {
        List<com.example.entity.vo.Orders> orders = ordersMapper.getAllOrderByUserId(userId, searchCon);
        for (com.example.entity.vo.Orders order : orders) {
            order.setTotalMoney(order.getProductPrice() * order.getOrderAmount());
        }
        return orders;
    }

    @Override
    public Boolean valuationOrderByOrderNumber(String orderNumber, String orderValuation) {
        return ordersMapper.valuationOrderByOrderNumber(orderNumber, orderValuation);
    }

    @Override
    public Boolean sendProductByOrderNumber(String orderNumber) {
        Orders order = ordersMapper.selectOne(new QueryWrapper<Orders>().eq("order_number", orderNumber));
        if (order.getOrderIsGroup()) {
            if (!order.getOrderIsGroupEnd()) {
                DateTime newDate = DateUtil.offsetHour(order.getOrderCreateTime(), 3);
                if (newDate.isAfter(new Date())) {
                    throw new CustomException("拼单未结束");
                }
            }
        }
        return ordersMapper.sendProductByOrderNumber(orderNumber);
    }

    @Override
    public Boolean receiptProductByOrderNumber(String orderNumber) {
        return ordersMapper.receiptProductByOrderNumber(orderNumber);
    }

    @Override
    public Boolean deleteOrdersByOrderNumber(String orderNumber) {
        return ordersMapper.deleteOrdersByOrderNumber(orderNumber);
    }

    @Override
    public List<com.example.entity.vo.Orders> getAllOrder(String searchCon) {
        List<com.example.entity.vo.Orders> orders = ordersMapper.getAllOrder(searchCon);
        for (com.example.entity.vo.Orders order : orders) {
            order.setTotalMoney(order.getProductPrice() * order.getOrderAmount());
        }
        return orders;
    }

    @Override
    public String getOrderValuationByProductId(Integer productId) {
        Set<String> set = productsMapper.getOrderValuationByProductId(productId);
        if (!set.isEmpty()) {
//            Iterator<String> iterator = set.iterator();
//            while (iterator.hasNext()) {
//                String next = iterator().next();
//                if (!Objects.isNull(next)) {
//                    return next;
//                }
//            }
            for (String next : set) {
                if (!Objects.isNull(next)) {
                    return next;
                }
            }
        }
        return null;
    }
}
