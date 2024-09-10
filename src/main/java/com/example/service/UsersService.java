package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Users;
import java.util.List;

/**
 * (Users)表服务接口
 *
 * @author makejava
 * @since 2022-03-24 13:55:55
 */
public interface UsersService extends IService<Users> {

    void deleteCartByUsernameAndPid(String username, String productId);

    void deleteCartByUserIdAndPid(String userId, String productId);

    void clearCart(String username);
}
