package com.kuang.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info("异常信息为:=="+ex.getMessage());
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            String[] strings = message.split(" ");
            return  R.error(strings[2].substring(1,strings[2].length()-1)+" 重复了");
        }
        return  R.error("未知的异常");
    }


    @ExceptionHandler(MyException.class)
    public R MyExceptionHandler(MyException ex){
        String message = ex.getMessage();
        return  R.error(message);
    }
}
