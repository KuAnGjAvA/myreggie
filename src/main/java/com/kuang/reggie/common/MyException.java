package com.kuang.reggie.common;

public class MyException extends RuntimeException{
    public MyException(String msg){
        super(msg);
    }
    public MyException(){
        super();
    }
}
