package com.kuang.reggie.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuang.reggie.common.BaseContext;
import com.kuang.reggie.common.R;
import com.kuang.reggie.entity.Employee;
import com.kuang.reggie.service.EmployeeService;
import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    //http://localhost:8080/employee/login
    @Autowired
    EmployeeService employeeService;

    /**
     * 管理登录
     * @param employee
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R login(@RequestBody Employee employee,
                   HttpServletRequest request) {
        HttpSession session = request.getSession();
        Employee loginEmployee = employeeService.login(employee.getUsername(), employee.getPassword());
        if (loginEmployee.getStatus() != 1) {
            return R.error("账号已被禁用");
        }
        if (loginEmployee != null) {
            Long aLong = employeeService.selectIdByUserName(employee.getUsername());
            session.setAttribute("loginId", aLong);
            //将登录的id保存到ThreadLocal
            BaseContext.setCurrentId(aLong);
            log.info("当前登录的id为=====>"+BaseContext.getCurrent());
            return R.success(loginEmployee);
        } else {
            return R.error("用户名或密码错误");
        }
    }

//    http://localhost:8080/employee/logout

    /**
     * 员工退出登录
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long loginId = (Long) session.getAttribute("loginId");
        if (loginId == null) {
            return R.error("当前未登录");
        } else {
            //将保存到ThreadLocal的登录id删除
            BaseContext.removeCurrent();
            session.removeAttribute("loginEmployee");
            return R.success("退出成功");
        }
    }

    //员工管理
//    http://localhost:8080/employee/page?page=1&pageSize=10
//    http://localhost:8080/employee/page?page=1&pageSize=10&name=%E6%A2%81%E6%9F%B1%E7%8B%82
    @GetMapping("/page")
    public R page(@RequestParam("page") Integer page,
                  @RequestParam("pageSize") Integer pageSize,
                  @RequestParam(value = "name", required = false) String name) {
        Page<Employee> employeePage = new Page<Employee>(page, pageSize);
        IPage iPage = employeeService.selectEmployeeByPage(employeePage, name);
        return R.success(iPage);
    }


    //添加员工
//    http://localhost:8080/employee
    @PostMapping
    public R addEmployee(@RequestBody Employee employee, HttpServletRequest request) {
        log.info("添加员工的threadLocal为"+BaseContext.getCurrent());
        //设置默认密码123456
        String password = DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8));
        employee.setPassword(password);
        HttpSession session = request.getSession();
        Long loginId = (Long) session.getAttribute("loginId");
        log.info("==================新增员工的信息为====={}", employee);
        Integer integer = employeeService.addEmploy(employee);
            return R.success("新增员工成功");
    }

    /**
     * 通过id查找员工信息
     *
     * @param id
     * @return
     */
    //http://localhost:8080/employee/1547195720820101000
    @GetMapping("/{id}")
    public R getEmployeeById(@PathVariable("id") Long id) {
        log.info("修改员工信息的id为{}"+id);


        Employee employee = employeeService.selectEmployeeById(id);
        log.info("修改之前的信息为{}"+employee);
        return R.success(employee);
    }

    //修改员工数据
    // http://localhost:8080/employee
    //http://localhost:8080/employee
    @PutMapping
    public R updateEmployee(@RequestBody Employee employee,HttpServletRequest request) {

        return R.success(employeeService.updateEmploy(employee));
    }


}
