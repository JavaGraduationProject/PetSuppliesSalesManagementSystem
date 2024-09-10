package com.example.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (Activity)实体类
 *
 * @author oceans
 * @since 2022-04-17 19:19:43
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Activity implements Serializable {
    private static final long serialVersionUID = -68250454628050808L;

    @TableId
    private Integer id;

    private String info;

    private Boolean isDelete;

}
