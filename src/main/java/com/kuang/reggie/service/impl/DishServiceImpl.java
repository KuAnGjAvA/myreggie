package com.kuang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.reggie.dto.DishDto;
import com.kuang.reggie.entity.Dish;
import com.kuang.reggie.entity.DishFlavor;
import com.kuang.reggie.mapper.DishMapper;
import com.kuang.reggie.service.DishFlavorService;
import com.kuang.reggie.service.DishService;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    DishService dishService;

    @Autowired
    DishFlavorService dishFlavorService;


    /**
     * 添加新菜品并保存对应的口味
     *
     * @param dishDto
     * @return
     */
    @Override
    public boolean addDishAndDishFlavor(DishDto dishDto) {
        this.save(dishDto);   //保存菜品
        //添加菜品后id为
        log.info("添加菜品后的菜品id为{}", dishDto.getId());
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (!flavors.isEmpty()) {
            //添加口味前为 每个口味添加上一个对应菜品的id
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dishDto.getId());
            }
            //添加菜品口味
            dishFlavorService.saveBatch(flavors);
        }
        return true;
    }


    @Override
    public void updateDishAndDishFlavor(DishDto dishDto) {
        //修改菜品
        dishService.updateById(dishDto);

        //根据菜品id删除所有的口味
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        dishFlavorQueryWrapper.eq("dish_id", dishDto.getId());
        dishFlavorService.remove(dishFlavorQueryWrapper);

        //添加新口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        if (!flavors.isEmpty()) {
            //添加口味前为 每个口味添加上一个对应菜品的id
            for (DishFlavor dishFlavor : flavors) {
                dishFlavor.setDishId(dishDto.getId());
            }
            //添加菜品口味
            dishFlavorService.saveBatch(flavors);
        }
    }

    @Override
    public DishDto selectDishAndDishFlavor(Long id) {
        DishDto dishDto = new DishDto();
        //获取菜品
        Dish dish = super.getById(id);
        BeanUtils.copyProperties(dish, dishDto);
        //获取口味
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        dishFlavorQueryWrapper.eq("dish_id", id);
        List<DishFlavor> list = dishFlavorService.list(dishFlavorQueryWrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }


    //删除菜品和对应的口味
    @Override
    public void deleteDishAndDishFlavor(Long[] ids) {
        QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
        //删除口味
        for (Long id : ids) {
            dishFlavorQueryWrapper.eq("dish_id", id);
            dishFlavorService.remove(dishFlavorQueryWrapper);
        }
        //删除菜品
        dishService.removeByIds(Arrays.asList(ids));
    }


    //起售或者停售单个或  批量起售或停售
    @Override
    public void statusDish(Integer status, Long[] ids) {
        for (Long id : ids) {
            //通过id获取菜品的所有信息
            Dish dish = dishService.getById(id);
            //修改dish属性
            dish.setStatus(status);
            //将修改的dish保存到数据库中
            dishService.updateById(dish);
        }
    }
}
