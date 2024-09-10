package com.example.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.util.Date;

/**
 * (Products)实体类
 *
 * @author makejava
 * @since 2022-03-24 14:11:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Products implements Serializable {

    private Integer productId;

    private String productName;

    private String productStyle;

    private String productDescribe;

    private Double productPrice;

    private String productNumber;

    private Integer productAmount;

    private String productCategory;

    private Date productCreateTime;

    private Date productUpdateTime;

    private Boolean productIsDelete;

    private byte[] productImg;

    private String url;

    private String orderValuation;

}
