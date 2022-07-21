package com.kuang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.reggie.common.MyException;
import com.kuang.reggie.dto.SetmealDto;
import com.kuang.reggie.entity.Dish;
import com.kuang.reggie.entity.Setmeal;
import com.kuang.reggie.entity.SetmealDish;
import com.kuang.reggie.mapper.SetmealMapper;
import com.kuang.reggie.service.DishService;
import com.kuang.reggie.service.SetmealDishService;
import com.kuang.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    SetmealService setmealService;

    @Autowired
    SetmealDishService setmealDishService;


    @Autowired
    DishService dishService;

    //添加套餐和相应的菜品
    @Override
    public void addSetmealAndSetmealDish(SetmealDto setmealDto) {
        //1、添加套餐
        setmealService.save(setmealDto);
        //2、添加套餐对应的菜品
        //获取所有的菜品（里面没有套餐id）
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        if(setmealDishes==null)
            throw new MyException("没有添加菜品");

        //获取套餐id
        Long setmealId = setmealDto.getId();

        for(SetmealDish sd : setmealDishes ){
            //获取通过菜品名获取菜品id
            QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
            dishQueryWrapper.eq("name",sd.getName());
            Dish dish = dishService.getOne(dishQueryWrapper);
            Long dishId = dish.getId();

            //设置菜品id
            sd.setDishId(dishId);
            //设置套餐id
            sd.setSetmealId(setmealId);
        }
        setmealDishService.saveBatch(setmealDishes);
    }


    //通过套餐id获取套餐数据和对应的菜品数据
    @Override
    public SetmealDto getSetmealAndSetmelDishById(Long id) {
        SetmealDto setmealDto = new SetmealDto();

        //1、获取套餐
        Setmeal setmeal = setmealService.getById(id);
        //将套餐存入SetmealDto
        BeanUtils.copyProperties(setmeal,setmealDto);

        //2、获取套餐对应的菜品
        //通过套餐id获取所有的菜品
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.eq("setmeal_id",id);
        List<SetmealDish> list = setmealDishService.list(setmealDishQueryWrapper);
        //将所有的菜品添加到setmealDto
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    //修改套餐和对应的菜品数据
    @Override
    public void updateSetmealAndSetmelDishById(SetmealDto setmealDto) {

        //修改套餐表
        setmealService.updateById(setmealDto);

        //删除之前套餐id对应的所有菜品
        QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
        setmealDishQueryWrapper.eq("setmeal_id",setmealDto.getId());
        setmealDishService.remove(setmealDishQueryWrapper);

        //获取套餐对应的所有的菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        //如果将套餐的菜品清空则报错误
        if(setmealDishes==null)
            throw new MyException("套餐的菜品不能为空");

        //为套餐对应的所有的菜品设置套餐id和菜品id
        //获取套餐id
        Long setmealId = setmealDto.getId();
        for(SetmealDish sd : setmealDishes ){
            //获取通过菜品名获取菜品id
            QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
            dishQueryWrapper.eq("name",sd.getName());
            Dish dish = dishService.getOne(dishQueryWrapper);
            Long dishId = dish.getId();

            //设置菜品id
            sd.setDishId(dishId);
            //设置套餐id
            sd.setSetmealId(setmealId);
        }
        //添加或者修改套餐对应的所有的菜品
        setmealDishService.saveBatch(setmealDishes);
    }

    //删除套餐和对应的菜品数据
    @Override
    public void deleteSetmealAndSetmelDishById(Long[] ids) {

        for(Long id : ids){
            //删除菜品
            QueryWrapper<SetmealDish> setmealDishQueryWrapper = new QueryWrapper<>();
            setmealDishQueryWrapper.eq("setmeal_id",id);
            setmealDishService.remove(setmealDishQueryWrapper);
        }
        //删除套餐
        setmealService.removeByIds(Arrays.asList(ids));
    }


    //起售或者停售单个套餐或  批量起售或停售套餐
    @Override
    public void statusSetmeal(Integer status, Long[] ids) {
        for(Long id : ids){
           //获取套餐信息
            Setmeal setmeal = setmealService.getById(id);
            setmeal.setStatus(status);
            //修改套餐信息
            setmealService.updateById(setmeal);
        }
    }
}
