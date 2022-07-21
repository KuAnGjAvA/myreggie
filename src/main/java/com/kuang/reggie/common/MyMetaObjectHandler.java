package com.kuang.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {


    @Autowired
    HttpServletRequest httpServletRequest;

    @Override
    public void insertFill(MetaObject metaObject) {

//        log.info("当前登录的id为=====>" + BaseContext.getCurrent());
        HttpSession session = httpServletRequest.getSession();
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        //判断是不是后台登录的
        if (session.getAttribute("loginId") != null) {
            metaObject.setValue("createUser", session.getAttribute("loginId"));
            metaObject.setValue("updateUser", session.getAttribute("loginId"));
            return;
        }
        //判断是不是用户登录
        if (session.getAttribute("user") != null) {
            metaObject.setValue("createUser", session.getAttribute("user"));
            metaObject.setValue("updateUser", session.getAttribute("user"));
            return;
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        HttpSession session = httpServletRequest.getSession();
        log.info("当前登录的id为=====>" + session.getAttribute("loginId"));

        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", session.getAttribute("loginId"));

        //判断是不是后台登录的
        if (session.getAttribute("loginId") != null) {
            metaObject.setValue("updateUser", session.getAttribute("loginId"));
            return;
        }
        //判断是不是用户登录
        if (session.getAttribute("user") != null) {
            metaObject.setValue("updateUser", session.getAttribute("user"));
            return;
        }
    }
}
