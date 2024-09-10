package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.io.Serializable;

/**
 * (Orders)实体类
 *
 * @author makejava
 * @since 2022-03-24 16:55:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders implements Serializable {
    private static final long serialVersionUID = 676710221565364428L;

    @TableId
    private Integer orderId;
    /**
    * 评价
    */
    private String orderValuation;
    /**
    * 订单编号
    */
    private String orderNumber;
    /**
    * 订单数量
    */
    private Integer orderAmount;
    /**
    * 是否付款
    */
    private Boolean orderIsPay;
    /**
    * 收货状态
    */
    private String orderIsReceipt;
    /**
    * 是否结束组团
    */
    private Boolean orderIsGroupEnd;
    /**
    * 是否组团
    */
    private Boolean orderIsGroup;
    /**
    * 是否评价
    */
    private Boolean orderIsEvaluate;

    private Date orderCreateTime;

    private Date orderUpdateTime;

    private Boolean orderIsDelete;


}
