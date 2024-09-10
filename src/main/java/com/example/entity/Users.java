package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.util.Date;
import java.io.Serializable;

/**
 * (Users)实体类
 *
 * @author makejava
 * @since 2022-03-24 13:55:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
    private static final long serialVersionUID = 225599958036921154L;

    @TableId
    private Integer userId;

    @NotNull
    @Length(min = 1,max = 15,message = "用户名长度不符合要求")
    private String username;

    @NotNull
    @Length(min = 1,max = 100,message = "收货地址长度不符合要求")
    private String address;

    @Email(message = "邮箱不符合要求")
    private String email;

    @NotNull
    @Positive(message = "电话号码必须是11位")
    @Length(min = 11,max = 11,message = "电话号码必须是11位")
    private String phone;

    @NotNull
    @Length(min = 6,max = 18,message = "由数字和26个英文字母组成长度在6到18位的字符串")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "由数字和26个英文字母组成长度在6到18位的字符串")
    private String password;

    private Date userCreateTime;

    private Date userUpdateTime;

    private String role;

    private String collection;

    private String cart;

    private Boolean isDelete;


}
