package com.kuang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.reggie.entity.Category;

public interface CategoryService extends IService<Category> {

    //通过id删除菜品或套餐分类
    public void deleteCategory(Long id);
}
