package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kuang.reggie.common.R;
import com.kuang.reggie.entity.User;
import com.kuang.reggie.service.UserService;
import com.kuang.reggie.utils.SMSUtils;
import com.kuang.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;


    //http://localhost:8080/user/sendMsg  === Post
    //获取验证码
    @PostMapping("/sendMsg")
    public R getMsgCode(@RequestBody User user, HttpServletRequest request) {
        //获取电话号码
        String phone = user.getPhone();

        if (!StringUtils.isEmpty(phone)) {
            //获取验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

            //将验证码保存到session key:phone value:code
            HttpSession session = request.getSession();
            session.setAttribute(phone, code);

            log.info("号码为{},的验证码为===================={}", phone, code);
            return R.success("获取验证码成功");
        }
        return R.error("获取验证码失败");

    }

    //用户登录
    //http://localhost:8080/user/login   == post
    @PostMapping("/login")
    public R login(@RequestBody Map map, HttpServletRequest request) {
        //获取手机号码和页面输入的验证码
        String phone = (String) map.get("phone");
        String code = (String) map.get("code");

        //将页面获取的数据与session进行比对
        HttpSession session = request.getSession();
        String attribute = (String) session.getAttribute(phone);

        if (attribute == null || attribute.equals("")) {
            R.error("请先获取验证码后登录");
        }
        if (code != null && attribute.equals(code)) {
            //验证通过
            //判断是不是新用户,如果是这注册该用户,否则返回用户信息
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("phone", phone);
            User one = userService.getOne(userQueryWrapper);
            //如果是新用户
            if (one == null) {
                User user = new User();
                user.setName(UUID.randomUUID().toString());
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
                session.removeAttribute("phone");
                log.info("新用户的id{}"+user.getId());
                //登录成功保存user key
                session.setAttribute("user",user.getId());
                return R.success(user);
            }
            //不是新用户直接登录
            session.removeAttribute("phone");
            session.setAttribute("user",one.getId());
            log.info("老用户的id{}"+one.getId());
            return R.success(one);
        }
        return R.error("请输入正确的验证码");
    }

    //退出登录
    //http://localhost:8080/user/loginout ==Post
    @PostMapping("/loginout")
    public R loginout(HttpServletRequest request){
        HttpSession session = request.getSession();
        Long userId = (Long)session.getAttribute("user");
        if(userId!=null){
            session.removeAttribute("user");
            return R.success("退出成功");
        }
        return R.error("退出失败");
    }


}
