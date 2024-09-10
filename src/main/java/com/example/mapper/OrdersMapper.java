package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    @Update("update orders set order_is_pay = true where order_number = #{orderNumber}")
    Boolean paymentByOrderNumber(String orderNumber);

    @Select("select product_id,order_create_time,product_name,product_style,product_describe,product_price,product_amount,product_create_time,order_id,order_number " +
            " from orders natural join order_product_user natural join products " +
            " where order_is_group = true and order_is_group_end = false and user_id != #{userId} and product_name like #{searchCon}")
    List<com.example.entity.vo.Orders> getAllGrouping(Integer userId, String searchCon);

    @Update("update orders set order_is_group_end = true where order_number = #{orderNumber}")
    Boolean updateOrderByOrderNumber(String orderNumber);

    @Select("select order_valuation,order_is_evaluate,order_is_receipt,order_is_pay,product_id,order_create_time,product_name,product_style,product_describe,product_price,product_amount,product_create_time,order_id,order_number,order_amount " +
            " from orders natural join order_product_user natural join products " +
            " where user_id = #{userId} and product_name like #{searchCon} and order_is_delete = false order by order_create_time desc")
    List<com.example.entity.vo.Orders> getAllOrderByUserId(Integer userId, String searchCon);

    @Update("update orders set order_valuation = #{orderValuation} where order_number = #{orderNumber}")
    Boolean valuationOrderByOrderNumber(String orderNumber, String orderValuation);

    @Update("update orders set order_is_receipt = '2' where order_number = #{orderNumber}")
    Boolean sendProductByOrderNumber(String orderNumber);

    @Update("update orders set order_is_receipt = '3' where order_number = #{orderNumber}")
    Boolean receiptProductByOrderNumber(String orderNumber);

    @Update("update orders set order_is_delete = true where order_number = #{orderNumber}")
    Boolean deleteOrdersByOrderNumber(String orderNumber);

    @Select("select order_valuation,order_is_evaluate,order_is_receipt,order_is_pay,product_id,order_create_time,product_name,product_style,product_describe,product_price,product_amount,product_create_time,order_id,order_number,order_amount " +
            " from orders natural join order_product_user natural join products " +
            " where product_name like #{searchCon} and order_is_delete = false")
    List<com.example.entity.vo.Orders> getAllOrder(String searchCon);
}
