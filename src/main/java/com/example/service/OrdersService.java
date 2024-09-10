package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Orders;

import java.util.List;

/**
 * (Orders)表服务接口
 *
 * @author makejava
 * @since 2022-03-24 16:55:10
 */
public interface OrdersService extends IService<Orders> {

    Orders generateOrder(Integer productId, Integer productAmount_m, Boolean isGroup, Integer userId, Boolean isCart);

    Orders paymentByOrderNumber(String orderNumber);

    List<com.example.entity.vo.Orders> getAllGrouping(Integer userId, String searchCon);

    Orders addGenerateOrder(Integer productId, Integer productAmount, Integer userId);

    Boolean updateOrderByOrderNumber(String orderNumber);

    List<com.example.entity.vo.Orders> getAllOrderByUserId(Integer userId, String searchCon);

    Boolean valuationOrderByOrderNumber(String orderNumber, String orderValuation);

    Boolean sendProductByOrderNumber(String orderNumber);

    Boolean receiptProductByOrderNumber(String orderNumber);

    Boolean deleteOrdersByOrderNumber(String orderNumber);

    List<com.example.entity.vo.Orders> getAllOrder(String searchCon);

    String getOrderValuationByProductId(Integer productId);
}

