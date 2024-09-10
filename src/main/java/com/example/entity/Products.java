package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;
import java.io.Serializable;

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
    private static final long serialVersionUID = 771421129548508894L;

    @TableId
    private Integer productId;
    /**
    * 产品名称
    */
    @NotNull(message = "产品名称不能为空")
    @Length(min = 1,max = 45,message = "产品名称长度不符合要求")
    private String productName;
    /**
    * 产品类型
    */
    @NotNull(message = "产品类型不能为空")
    @Length(min = 1,max = 45,message = "产品类型长度不符合要求")
    private String productStyle;
    /**
    * 产品描述
    */
    @NotNull(message = "产品描述不能为空")
    @Length(min = 1,max = 255,message = "产品描述长度不符合要求")
    private String productDescribe;
    /**
    * 产品价格
    */
    @NotNull(message = "产品价格不能为空")
    private Double productPrice;
    /**
    * 产品编号
    */
    private String productNumber;
    /**
    * 产品数量
    */
    @NotNull(message = "产品数量不能为空")
    @Positive(message = "产品数量必须是数字")
    private Integer productAmount;
    /**
    * 产品类别
    */
    private String productCategory;
    /**
    * 上架时间
    */
    private Date productCreateTime;
    /**
    * 修改时间
    */
    private Date productUpdateTime;
    /**
    * 产品是否删除
    */
    private Boolean productIsDelete;

    private byte[] productImg;

}
