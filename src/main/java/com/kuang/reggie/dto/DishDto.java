package com.kuang.reggie.dto;
import com.kuang.reggie.entity.Dish;
import com.kuang.reggie.entity.DishFlavor;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class DishDto extends Dish {


    //菜品口味列表
    private List<DishFlavor> flavors = new ArrayList<>();

    private String categoryName;

    private Integer copies;
}
