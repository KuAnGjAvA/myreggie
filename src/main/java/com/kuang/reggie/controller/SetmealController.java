package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.reggie.common.R;
import com.kuang.reggie.dto.SetmealDto;
import com.kuang.reggie.entity.Setmeal;
import com.kuang.reggie.entity.SetmealDish;
import com.kuang.reggie.service.CategoryService;
import com.kuang.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Autowired
    SetmealService setmealService;

    @Autowired
    CategoryService categoryService;

    //显示套餐
    //http://localhost:8080/setmeal/page?page=1&pageSize=10&name=%E7%9A%84
    @GetMapping("/page")
    public R<IPage> selectAllSetmeal(Integer page, Integer pageSize, String name) {
        Page<Setmeal> setmealPage = new Page<>();
        if (name == null || name.trim().equals("")) {
            setmealService.page(setmealPage);
        } else {
            QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
            setmealQueryWrapper.like("name", name.trim());
            setmealService.page(setmealPage, setmealQueryWrapper);
        }
        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        //用来存储所有的显示在页面的数据
        List<SetmealDto> setmealDtoArrayList = new ArrayList<SetmealDto>();
        List<Setmeal> records = setmealPage.getRecords();
        for (Setmeal setmeal : records) {
            //存储当前的一条数据
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(setmeal,setmealDto);
            //获取套餐id
            Long categoryId = setmeal.getCategoryId();
            //通过套餐id等到套餐的类名称
            String categoryName = categoryService.getById(categoryId).getName();
            setmealDto.setCategoryName(categoryName);
            setmealDtoArrayList.add(setmealDto);
        }
        setmealDtoPage.setRecords(setmealDtoArrayList);
        return R.success(setmealDtoPage);
    }

    //http://localhost:8080/setmeal
    //添加套餐
    @PostMapping
    public R addSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.addSetmealAndSetmealDish(setmealDto);
        return R.success("添加成功");
    }


    //    http://localhost:8080/setmeal/list?categoryId=1413386191767674881&status=1
    //通过套餐id获取数据
    //http://localhost:8080/setmeal/1415580119015145474
    @GetMapping("/{id}")
    public R getSetmealById(@PathVariable("id")Long id){
        //获取套餐
        SetmealDto setmealAndSetmelDishById = setmealService.getSetmealAndSetmelDishById(id);
        return R.success(setmealAndSetmelDishById);

    }

    //修改套餐
    //http://localhost:8080/setmeal
    @PutMapping
    public R updateSetmeal(@RequestBody SetmealDto setmealDto){
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        log.info("====={}",setmealDishes.size());
        setmealService.updateSetmealAndSetmelDishById(setmealDto);
        return R.success("修改成功");
    }

    //删除一个或多个(批量)套餐
    //http://localhost:8080/setmeal?ids=1547916455721041921
    @DeleteMapping
    public R deleteSetmeal(Long[] ids){
        setmealService.deleteSetmealAndSetmelDishById(ids);
        return R.success("删除成功");
    }


    //http://localhost:8080/setmeal/status/0?ids=1415580119015145474
    //起售或者停售单个套餐或  批量起售或停售套餐
    @PostMapping("/status/{status}")
    public R statusSetmeal(@PathVariable("status") Integer status, Long[] ids){
        setmealService.statusSetmeal(status,ids);
        return R.success("操作成功");
    }


    //http://localhost:8080/setmeal/list?categoryId=1413342269393674242&status=1
    //http://localhost:8080/setmeal/list?categoryId=1413342269393674242&status=1 === Get
    //通过套餐类别  id和status(是否停用)获取套餐
    @GetMapping("/list")
    public R list(Setmeal setmeal){

        QueryWrapper<Setmeal> setmealQueryWrapper = new QueryWrapper<>();
        if(setmeal.getStatus()!=null){
            setmealQueryWrapper.eq("status",setmeal.getStatus());
        }
        if(setmeal.getCategoryId()!=null){
            setmealQueryWrapper.eq("category_id",setmeal.getCategoryId());
        }
        setmealQueryWrapper.orderByDesc("update_time");
        List<Setmeal> list = setmealService.list(setmealQueryWrapper);
        return  R.success(list);
    }


}
