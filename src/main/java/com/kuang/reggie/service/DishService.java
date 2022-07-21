package com.kuang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.reggie.dto.DishDto;
import com.kuang.reggie.entity.Dish;

public interface DishService extends IService<Dish> {

    //添加新菜品并保存对应的口味
    public boolean addDishAndDishFlavor(DishDto dishDto);

    //修改菜品和对应的口味
    public void updateDishAndDishFlavor(DishDto dishDto);


    //修改前获取菜品和对应的口味
    public DishDto selectDishAndDishFlavor(Long id);

    //删除菜品和对应的口味
    public void deleteDishAndDishFlavor(Long[] ids);

    //起售或者停售单个或  批量起售或停售
    public void statusDish(Integer status,Long[] ids);

}
