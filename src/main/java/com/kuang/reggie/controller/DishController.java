package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.reggie.common.R;
import com.kuang.reggie.dto.DishDto;
import com.kuang.reggie.entity.Category;
import com.kuang.reggie.entity.Dish;
import com.kuang.reggie.entity.DishFlavor;
import com.kuang.reggie.service.CategoryService;
import com.kuang.reggie.service.DishFlavorService;
import com.kuang.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    DishService dishService;

    @Autowired
    CategoryService categoryService;


    @Autowired
    DishFlavorService dishFlavorService;

    //显示页面数据
    //http://localhost:8080/dish/page?page=1&pageSize=10&name=%E5%A4%9A%E7%A6%8F%E5%A4%9A%E5%AF%BF
    @GetMapping("/page")
    public R selectAllDish(Integer page, Integer pageSize, String name) {
        Page<Dish> page1 = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();
        if (name == null || name.trim().equals("")) {
            dishService.page(page1);
        } else {
            QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
            dishQueryWrapper.like("name", name.trim());
            dishService.page(page1, dishQueryWrapper);
        }
        //将page1 的数据拷贝到 dishDtoPage
        BeanUtils.copyProperties(page1, dishDtoPage, "records");
        //获取dishDtoPage的 List<T> records 属性，代表页面的列表属性
        List<Dish> records = page1.getRecords();
        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish dish : records) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
            Long categoryId = dish.getCategoryId();
            Category byId = categoryService.getById(categoryId);
            if (byId != null) {
                String categoryName = byId.getName();
                dishDto.setCategoryName(categoryName);
                dishDtoList.add(dishDto);
            }
        }
        log.info("列表信息为{}", dishDtoList);
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    //添加菜品
    //http://localhost:8080/dish
    @PostMapping
    public R addDish(@RequestBody DishDto dishDto) {
        dishService.addDishAndDishFlavor(dishDto);
        return R.success("添加菜品成功");
    }


    //通过菜品获取菜品数据和菜品口味
    //http://localhost:8080/dish/1397849739276890114
    @GetMapping("/{id}")
    public R selectDish(@PathVariable("id") Long id) {
        DishDto dishDto = dishService.selectDishAndDishFlavor(id);
        return R.success(dishDto);
    }

    //修改菜品菜品
    //http://localhost:8080/dish
    @PutMapping
    public R updateDish(@RequestBody DishDto dishDto) {
        log.info("获取要修改的数================据为{}", dishDto);
        dishService.updateDishAndDishFlavor(dishDto);
        return R.success("修改成功");
    }


    //删除单个或者多个菜品
    //http://localhost:8080/dish?ids=1397853709101740034
    @DeleteMapping
    public R deleteDish(Long[] ids) {
        dishService.deleteDishAndDishFlavor(ids);
        return R.success("删除成功");
    }

    //起售或者停售单个或  批量起售或停售
    //http://localhost:8080/dish/status/1?ids=139784973927689011 == post
    @PostMapping("/status/{status}")
    public R statusDish(@PathVariable("status") Integer status, Long[] ids) {
        dishService.statusDish(status, ids);
        return R.success("操作成功");
    }


    //获取指定菜品类别id  获取所有的菜品     和口味数据
    //http://localhost:8080/dish/list?categoryId=1397844263642378242
//    @GetMapping("/list")
//    public R dishList(Long categoryId) {
//        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
//        dishQueryWrapper.eq("category_id",categoryId);
//        List<Dish> list = dishService.list(dishQueryWrapper);
//        return R.success(list);
//    }


    //改造
    //获取指定菜品类别id  获取所有的菜品     和口味数据
    //http://localhost:8080/dish/list?categoryId=1397844263642378242
    @GetMapping("/list")
    public R dishDtoList(Dish dish){
        QueryWrapper<Dish> dishQueryWrapper = new QueryWrapper<>();
        dishQueryWrapper.eq("category_id",dish.getCategoryId());
        List<Dish> list = dishService.list(dishQueryWrapper);
        List<DishDto> listDishDto = new ArrayList<>();
        for(Dish ds : list){
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(ds, dishDto);
            //获取口味列表
            //获取菜品id
            Long dishId = ds.getId();
            //通过菜品id该菜品的所有的口味
            QueryWrapper<DishFlavor> dishFlavorQueryWrapper = new QueryWrapper<>();
            dishFlavorQueryWrapper.eq("dish_id",dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorQueryWrapper);
            //将该菜品id该菜品的所有的口味添加到dishDto
            dishDto.setFlavors(dishFlavorList);
            listDishDto.add(dishDto);
        }
        return R.success(listDishDto);
    }


}
