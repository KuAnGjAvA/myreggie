//package com.kuang.reggie.utils;
//
//import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpSession;
//
///**
// * 获取用户或者员工登录的id
// */
//@Component
//public class SessionUtils {
//
//    @Autowired
//    public static HttpServletRequest request;
//
//    //获取用户登录的id
//    public static Long getUserLoginId() {
//        HttpSession session = request.getSession();
//        return (Long)session.getAttribute("user");
//    }
//
//    //添加用户登录的id
//    public static void addUserLoginId(Long userId) {
//        HttpSession session = request.getSession();
//        session.setAttribute("user",userId);
//    }
//
//    //删除用户登录的id
//    public static void deleteUserLoginId(Long userId) {
//        HttpSession session = request.getSession();
//        session.removeAttribute("user");
//    }
//
//}
