package com.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.OrderProductUser;
import com.example.mapper.OrderProductUserMapper;
import com.example.service.OrderProductUserService;
import org.springframework.stereotype.Service;

/**
 * (OrderProductUser)表服务实现类
 *
 * @author makejava
 * @since 2022-03-24 16:55:10
 */
@Service("orderProductUserService")
public class OrderProductUserServiceImpl extends ServiceImpl<OrderProductUserMapper, OrderProductUser> implements OrderProductUserService {

}
