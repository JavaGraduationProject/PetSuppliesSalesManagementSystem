package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Products;
import java.util.List;
import java.util.Map;

/**
 * (Products)表服务接口
 *
 * @author makejava
 * @since 2022-03-24 14:11:47
 */
public interface ProductsService extends IService<Products> {

    Boolean deleteProductById(String productId);

    Map<String, Object> getEchartsData(String searchYear, String searchMonth);
}
