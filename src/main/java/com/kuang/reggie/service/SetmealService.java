package com.kuang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.reggie.dto.SetmealDto;
import com.kuang.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {

    //添加套餐和相应的菜品
    public void addSetmealAndSetmealDish(SetmealDto setmealDto);

    //通过套餐id获取套餐数据和对应的菜品数据
    public SetmealDto getSetmealAndSetmelDishById(Long id);

    //修改套餐和对应的菜品数据
    public void updateSetmealAndSetmelDishById(SetmealDto setmealDto);

    //删除套餐和对应的菜品数据
    public void deleteSetmealAndSetmelDishById(Long[] ids);

    //起售或者停售单个套餐或  批量起售或停售套餐
    public void statusSetmeal( Integer status, Long[] ids);


}
