package com.kuang.reggie.common;

public class BaseContext {

    public static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    //存储数据
    public static void setCurrentId(Long longs) {
        threadLocal.set(longs);
    }

    //获取数据
    public static Long getCurrent() {
        Long aLong = threadLocal.get();
        return aLong;
    }

    //删除数据
    public static void removeCurrent() {
        threadLocal.remove();
    }
}
