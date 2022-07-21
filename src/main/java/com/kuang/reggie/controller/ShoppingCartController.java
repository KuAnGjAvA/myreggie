package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.reggie.common.R;
import com.kuang.reggie.entity.Dish;
import com.kuang.reggie.entity.Setmeal;
import com.kuang.reggie.entity.ShoppingCart;
import com.kuang.reggie.service.DishService;
import com.kuang.reggie.service.SetmealService;
import com.kuang.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;

    //减少一份菜品或者套餐
//    http://localhost:8080/shoppingCart/sub   ==Post
    @PostMapping("/sub")
    public R subShoppingCart(@RequestBody ShoppingCart shoppingCart1,HttpServletRequest request){
        //获取用户id
        Long userId = (Long) request.getSession().getAttribute("user");
        //通过用户id和(菜品id或者套餐id)获取完整的购物车信息
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id", userId);
        //判断是菜品还是套餐
        if (shoppingCart1.getDishId() != null) {
            //是菜品
            shoppingCartQueryWrapper.eq("dish_id", shoppingCart1.getDishId());
        } else {
            //是套餐
            shoppingCartQueryWrapper.eq("setmeal_id", shoppingCart1.getSetmealId());
        }
        ShoppingCart shoppingCart = shoppingCartService.getOne(shoppingCartQueryWrapper);
        //获取该菜品或者套餐点了多少份
        Integer number = shoppingCart.getNumber();

        //判断是否为最后一份，如果是最后一份则直接删除，否则菜品数量减少一份
        if(number>1){
            shoppingCart.setNumber(shoppingCart.getNumber()-1);
            shoppingCartService.updateById(shoppingCart);
        }else {
            shoppingCartService.removeById(shoppingCart.getId());
        }
        return R.success("操作成功");

    }

    //添加购物车 菜品或者是套餐
    //http://localhost:8080/shoppingCart/add  == POST
    @PostMapping("/add")
    public R addShoppingCart(@RequestBody ShoppingCart shoppingCart, HttpServletRequest request) {
        //获取用户id
        Long userId = (Long) request.getSession().getAttribute("user");
        shoppingCart.setUserId(userId);

        //先根据用户id 和 套餐id或菜品id获取数据判断之前是否存在
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id", userId);
        //判断是菜品还是套餐
        if (shoppingCart.getDishId() != null) {
            //是菜品
            shoppingCartQueryWrapper.eq("dish_id", shoppingCart.getDishId());
        } else {
            //是套餐
            shoppingCartQueryWrapper.eq("setmeal_id", shoppingCart.getSetmealId());
        }
        //判断是否是之前已经存在
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(shoppingCartQueryWrapper);
        if (shoppingCart1 == null) {
            //第一次添加
            //添加到购物车
            shoppingCartService.save(shoppingCart);
            return R.success("添加成功");
        }
        //之前就已经存在,则数量加1，金额添加
        //数量加一
        shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
        //保存修改的数据
        shoppingCartService.updateById(shoppingCart1);
        return R.success("添加成功");
    }

    //显示购物车列表
//    http://localhost:8080/shoppingCart/list  ==get
    @GetMapping("/list")
    public R getAllShoppingCart(HttpServletRequest request) {
        //获取用户id
        Long userId = (Long) request.getSession().getAttribute("user");
        //根据用户id获取所有购物车数据
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id", userId);
        List<ShoppingCart> list = shoppingCartService.list(shoppingCartQueryWrapper);
        return R.success(list);
    }

    //http://localhost:8080/shoppingCart/clean  ==DELETE
    //清空购物车
    @DeleteMapping("/clean")
    public R cleanShoppingCart(HttpServletRequest request) {
        //获取用户id
        Long userId = (Long) request.getSession().getAttribute("user");
        //根据用户id删除所有购物车数据
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id", userId);
        shoppingCartService.remove(shoppingCartQueryWrapper);
        return R.success("删除成功");
    }


}
