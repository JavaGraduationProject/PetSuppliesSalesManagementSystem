package com.example.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

    private Integer productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品类型
     */
    private String productStyle;
    /**
     * 产品描述
     */
    private String productDescribe;
    /**
     * 产品价格
     */
    private Double productPrice;
    /**
     * 产品数量
     */
    private Integer productAmount;

    private Date productCreateTime;

    private Integer orderId;
    /**
     * 订单编号
     */
    private String orderNumber;

    private Date orderCreateTime;

    private Double totalMoney;
    /**
     * 是否付款
     */
    private Boolean orderIsPay;
    /**
     * 收货状态
     */
    private String orderIsReceipt;
    /**
     * 是否评价
     */
    private Boolean orderIsEvaluate;
    /**
     * 评价
     */
    private String orderValuation;
    /**
     * 订单数量
     */
    private Integer orderAmount;

}
