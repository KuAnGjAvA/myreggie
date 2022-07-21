package com.kuang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.reggie.common.MyException;
import com.kuang.reggie.entity.*;
import com.kuang.reggie.mapper.OrdersMapper;
import com.kuang.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@Transactional
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {


    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    ShoppingCartService shoppingCartService;

    @Autowired
    UserService userService;

    @Autowired
    AddressBookService addressBookService;


    //{remark: "", payMethod: 1, addressBookId: "1548912434037514242"}
    @Override
    public void submit(Orders orders) {
        //获取用户id
        Long userId = (Long) request.getSession().getAttribute("user");

        //保存数据到orders,补充orders的数据
        Long orderId = IdWorker.getId();//获取订单号

        //根据用户id获取购物车数据
        QueryWrapper<ShoppingCart> shoppingCartQueryWrapper = new QueryWrapper<>();
        shoppingCartQueryWrapper.eq("user_id", userId);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(shoppingCartQueryWrapper);

        if (shoppingCartList == null) {
            throw new MyException("购物车为空,不能下单!");
        }

        //计算支付的金额
//        BigDecimal amount = new BigDecimal(0);
        AtomicInteger amount = new AtomicInteger(0);

        //遍历所有的订单
        //保存订单明细
        for (ShoppingCart shoppingCart : shoppingCartList) {
            //创建保存数据到订单明细表（orderDetail)
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrderId(orderId);
            orderDetail.setName(shoppingCart.getName());
            orderDetail.setDishId(shoppingCart.getDishId());
            orderDetail.setSetmealId(shoppingCart.getSetmealId());
            orderDetail.setDishFlavor(shoppingCart.getDishFlavor());
            orderDetail.setNumber(shoppingCart.getNumber());
            orderDetail.setAmount(shoppingCart.getAmount());
            orderDetail.setImage(shoppingCart.getImage());
            //保存订单明细
            orderDetailService.save(orderDetail);

            BigDecimal ShoppingAmount = shoppingCart.getAmount();  //单价

            BigDecimal number = new BigDecimal(shoppingCart.getNumber());//数量
            amount.addAndGet(ShoppingAmount.multiply(number).intValue());
        }

        //通过addressBookId获取用户地址信息
        AddressBook addressBook = addressBookService.getById(orders.getAddressBookId());

        //通过用户id获取用户信息
        User user = userService.getById(userId);
        orders.setId(orderId);
        orders.setNumber(Long.toString(orderId));
        orders.setStatus(2);
        orders.setUserId(userId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setAmount(new BigDecimal(amount.longValue()));   //实收金额
        orders.setUserName(user.getName());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress("详细地址为：" + addressBook.getDetail() + ",所在地址是一个：" + addressBook.getLabel()
                + " 收货人是：" + addressBook.getConsignee() + " 收货人电话为：" + addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        //保存订单
        ordersService.save(orders);

        //删除购物车数据,根据用户id删除购物车数据
        shoppingCartService.remove(shoppingCartQueryWrapper);
    }

    @Override
    public void next(Orders orders) {
        //获取订单id
        Long ordersId = orders.getId();
        //根据订单id获取该订单
        Orders order = ordersService.getById(ordersId);

        Orders newOrder = new Orders();       //创建新订单
        Long newOrderId = IdWorker.getId();   //获取订单号
        BeanUtils.copyProperties(order,newOrder,"id");
        //修改订单状态
        newOrder.setStatus(2);
        newOrder.setId(newOrderId);
        newOrder.setOrderTime(LocalDateTime.now());
        newOrder.setCheckoutTime(LocalDateTime.now());
        //添加新的订单
        ordersService.save(newOrder);

        //根据原来的订单id获取所有的订单明细
        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.eq("order_id",ordersId);
        List<OrderDetail> orderDetailList = orderDetailService.list(orderDetailQueryWrapper);
        //遍历所有的订单明细，将id设置为空，修改订单id,添加新的订单明细
        for(OrderDetail orderDetail : orderDetailList){
            //将id设置为空
            orderDetail.setId(null);
            //修改订单id
            orderDetail.setOrderId(newOrderId);
            //添加新的订单明细
            orderDetailService.save(orderDetail);
        }
    }
}
