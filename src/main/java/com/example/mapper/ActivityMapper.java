package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Activity;
import org.apache.ibatis.annotations.Mapper;

/**
 * (Activity)表数据库访问层
 *
 * @author oceans
 * @since 2022-04-17 19:19:43
 */
@Mapper
public interface ActivityMapper extends BaseMapper<Activity> {

}
