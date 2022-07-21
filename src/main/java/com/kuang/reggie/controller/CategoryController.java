package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.reggie.common.R;
import com.kuang.reggie.entity.Category;
import com.kuang.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    //http://localhost:8080/category/page?page=1&pageSize=10
    //分类管理页面显示
    @GetMapping("/page")
    public R selectCategoryByPage(@RequestParam("page")Integer page,
                                  @RequestParam("pageSize") Integer pageSize){
        Page<Category> CategoryPage = new Page<Category>(page, pageSize);
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.orderByAsc("sort");
        Page<Category> page1 = categoryService.page(CategoryPage,categoryQueryWrapper);
        return R.success(page1);
    }

//    http://localhost:8080/category
    //添加菜品分类或者套餐分类
    @PostMapping
    public R addCategory(@RequestBody Category category){
        log.info("获取到要修改的菜品为{}"+category);

        categoryService.save(category);
        return R.success("添加成功");
    }


    //http://localhost:8080/category
    //修改菜品
    @PutMapping
    public R updateCategory(@RequestBody Category category){
        log.info("获取到要修改的菜品为{}"+category);
        categoryService.updateById(category);
        return R.success("修改成功");
    }

//    http://localhost:8080/category?ids=1547483919194849281
    @DeleteMapping
    public R deleteCategory(@RequestParam("ids")Long ids){
        categoryService.deleteCategory(ids);
        return R.success("删除成功");
    }

    //根据菜品类型（1：菜品，2：套餐）显示指定的所有种类
//    http://localhost:8080/category/list?type=1
//    @GetMapping("/list")
//    public R addDish(Integer type){
//        //为空查询所有
//        if(type==null){
//            List<Category> list = categoryService.list();
//            return R.success(list);
//        }
//
//        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
//        categoryQueryWrapper.eq("type",type);
//        List<Category> list = categoryService.list(categoryQueryWrapper);
//        return R.success(list);
//    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
