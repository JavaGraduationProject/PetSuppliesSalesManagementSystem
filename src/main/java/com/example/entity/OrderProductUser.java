package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (OrderProductUser)实体类
 *
 * @author makejava
 * @since 2022-03-24 16:55:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductUser implements Serializable {
    private static final long serialVersionUID = 200513105756834730L;
    /**
    * 订单id
    */
    @TableId
    private Integer orderId;
    /**
    * 产品id
    */
    private Integer productId;
    /**
    * 用户id
    */
    private Integer userId;


}
