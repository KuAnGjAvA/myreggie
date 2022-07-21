package com.kuang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.reggie.common.MyException;
import com.kuang.reggie.entity.Category;
import com.kuang.reggie.mapper.CategoryMapper;
import com.kuang.reggie.service.CategoryService;
import com.kuang.reggie.service.DishService;
import com.kuang.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    DishService dishMapper;

    @Autowired
    SetmealService setmealMapper;

    @Override
    public void deleteCategory(Long id) {
        QueryWrapper dishQueryWrapper = new QueryWrapper();
        dishQueryWrapper.eq("category_id", id);
        //查找有没有菜品关联
        int count1 = dishMapper.count(dishQueryWrapper);

        //查找有没有套餐关联
        int count2 = setmealMapper.count(dishQueryWrapper);

        //存在菜品和套餐关联
        if (count1 > 0 && count2 > 0) {
            throw new MyException("存在菜品和套餐关联");
        }

        //存在关联掏出异常
        if (count1 > 0) {
            throw new MyException("存在菜品关联!");
        }

        //存在关联掏出异常
        if (count2 > 0) {
            throw new MyException("存在套餐关联!");
        }

        //没有存在菜品和套餐关联，删除该分类
        super.removeById(id);
    }
}
