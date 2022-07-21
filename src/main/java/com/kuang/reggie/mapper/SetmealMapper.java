package com.kuang.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kuang.reggie.dto.SetmealDto;
import com.kuang.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {

}
