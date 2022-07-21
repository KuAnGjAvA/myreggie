package com.kuang.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    //订单提交
    public void submit(Orders orders);

    //再下一单
    public void next(Orders orders);
}
