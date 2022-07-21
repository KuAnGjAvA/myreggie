package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.reggie.common.R;
import com.kuang.reggie.dto.OrdersDto;
import com.kuang.reggie.entity.OrderDetail;
import com.kuang.reggie.entity.Orders;
import com.kuang.reggie.entity.User;
import com.kuang.reggie.service.OrderDetailService;
import com.kuang.reggie.service.OrdersService;
import com.kuang.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    OrdersService ordersService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    UserService userService;

    //http://localhost:8080/order/submit
    //提交订单
    @PostMapping("/submit")
    public R submit(@RequestBody Orders orders) {
        ordersService.submit(orders);
        return R.success("提交成功");
    }


    //显示订单页面
    // page?page=1&pageSize=10&number=kfg&beginTime=2022-06-27%2000%3A00%3A00&endTime=2022-07-04%2023%3A59%3A59  ==GET
    @GetMapping("/page")
    public R page(Integer page, Integer pageSize, String number, String beginTime, String endTime) {
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();

        //搜索的订单号不为空
        if (number != null && !number.trim().equals("")) {
            ordersQueryWrapper.like("number", number);
        }
        //开始时间和结束时间不为空
        if ((beginTime != null && !beginTime.trim().equals(""))
                && (endTime != null && !endTime.trim().equals(""))) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime beginLocalDateTime = LocalDateTime.parse(beginTime, dateTimeFormatter);
            LocalDateTime endLocalDateTime = LocalDateTime.parse(endTime, dateTimeFormatter);
            ordersQueryWrapper.between("order_time", beginLocalDateTime, endLocalDateTime);
        }
        Page<Orders> orderList = ordersService.page(ordersPage, ordersQueryWrapper);
        return R.success(orderList);
    }

    //修改订单状态
    //http://localhost:8080/order
    @PutMapping
    public R updateOrderStatus(@RequestBody Orders orders) {
        UpdateWrapper<Orders> ordersUpdateWrapper = new UpdateWrapper<>();
        ordersUpdateWrapper.eq("id", orders.getId());
        ordersUpdateWrapper.set("status", orders.getStatus());
        ordersService.update(ordersUpdateWrapper);
        return R.success("操作成功");
    }

    //显示用户的订单数据
    //http://localhost:8080/order/userPage?page=1&pageSize=1 == GET
    @GetMapping("/userPage")
    public R getUserPage(Integer page, Integer pageSize, HttpServletRequest request) {

        Page<Orders> ordersPage = new Page<>(page, pageSize);
        Page<OrdersDto> ordersDtoPage = new Page<>();

        Long userId = (Long) request.getSession().getAttribute("user");   //获取用户id
        //获取用户信息
        User user = userService.getById(userId);

        //获取该用户的所有订单
        QueryWrapper<Orders> ordersQueryWrapper = new QueryWrapper<>();
        ordersQueryWrapper.eq("user_id", userId);
        ordersService.page(ordersPage, ordersQueryWrapper);
        BeanUtils.copyProperties(ordersPage, ordersDtoPage, "records");
        List<OrdersDto> ordersDtoList = new ArrayList<>();

        List<Orders> records = ordersPage.getRecords();
        for (Orders orders : records) {
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(orders, ordersDto);
            //获取该订单id
            Long ordersId = orders.getId();
            //通过订单id获取所有的商品详情
            QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
            orderDetailQueryWrapper.eq("order_id", ordersId);
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailQueryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            ordersDto.setUserName(user.getName());
            ordersDtoList.add(ordersDto);
            ordersDtoPage.setRecords(ordersDtoList);
        }
        return R.success(ordersDtoPage);
    }

    //在下一单
    //http://localhost:8080/order/again
    @PostMapping("/again")
    public R nextOrder(@RequestBody Orders orders) {
        ordersService.next(orders);
        return R.success("下单成功");
    }

}
