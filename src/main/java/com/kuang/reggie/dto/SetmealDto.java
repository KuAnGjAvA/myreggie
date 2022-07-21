package com.kuang.reggie.dto;

import com.kuang.reggie.entity.Setmeal;
import com.kuang.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    //套餐类名称
    private String categoryName;
}
