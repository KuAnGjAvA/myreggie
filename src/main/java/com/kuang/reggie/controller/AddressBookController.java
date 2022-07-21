package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.kuang.reggie.common.R;
import com.kuang.reggie.entity.AddressBook;
import com.kuang.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    AddressBookService addressBookService;

    //  获取所有的地址列表
    //  http://localhost:8080/addressBook/list====get
    @GetMapping("/list")
    public R list(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long userId = (Long) session.getAttribute("user");
        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id", userId);
        addressBookQueryWrapper.orderByAsc("create_time");
        List<AddressBook> list = addressBookService.list(addressBookQueryWrapper);
        return R.success(list);
    }


    //添加地址
//    http://localhost:8080/addressBook ===post
    @PostMapping
    public R addAddressBook(@RequestBody AddressBook addressBook, HttpServletRequest request) {
        Long userId = (Long) request.getSession().getAttribute("user");
        addressBook.setUserId(userId);
        addressBookService.save(addressBook);
        return R.success("添加成功");
    }

    //通过id查询地址信息
    //http://localhost:8080/addressBook/1548872624962891777  === GET
    @GetMapping("/{id}")
    public R selectAddressBookById(@PathVariable("id") Long id) {
        return R.success(addressBookService.getById(id));
    }

    //修改地址
    //http://localhost:8080/addressBook  === put
    @PutMapping
    public R updateAddressBook(@RequestBody AddressBook addressBook){
        log.info("获取修改前判断是否有用户id{}"+addressBook.getUserId());
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }

    //删除地址
    //http://localhost:8080/addressBook?ids=1548912434037514242 ==delete
    @DeleteMapping
    public R deleteAddressBook(Long ids){
        addressBookService.removeById(ids);
        return R.success("删除成功");
    }

    //设置为默认地址
    //http://localhost:8080/addressBook/default ==put
    @PutMapping("/default")
    public R setDefaultAddress(@RequestBody AddressBook addressBook,HttpServletRequest request){
        //获取当前登录的用户id
        Long userId =(Long)request.getSession().getAttribute("user");
        //将当前用户的所有的地址的是否为默认地址设置为否（0）
        UpdateWrapper<AddressBook> addressBookUpdateWrapper = new UpdateWrapper<>();
        addressBookUpdateWrapper.eq("user_id",userId);
        addressBookUpdateWrapper.set("is_default",0);
        addressBookService.update(addressBookUpdateWrapper);

        //修改当前获取到的addressBook 修改isDefault 为1,并进行修改
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return  R.success("设置成功");
    }

    //http://localhost:8080/addressBook/default   =GET
    //获取默认地址
    @GetMapping("/default")
    public R getDefaultAddress(HttpServletRequest request){
        //获取当前登录的用户id
        Long userId =(Long)request.getSession().getAttribute("user");
        QueryWrapper<AddressBook> addressBookQueryWrapper = new QueryWrapper<>();
        addressBookQueryWrapper.eq("user_id",userId);
        addressBookQueryWrapper.eq("is_default",1);
        AddressBook addressBook = addressBookService.getOne(addressBookQueryWrapper);
        return R.success(addressBook);
    }
}
