package com.kuang.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuang.reggie.entity.Employee;

import java.util.List;

public interface EmployeeService extends IService<Employee> {
    //员工登录
    public Employee login(String username,String password);

    //查询指定页码的员工信息
    public IPage selectEmployeeByPage(Page page,String name);

    //添加员工
    public Integer addEmploy(Employee employee);

    //删除员工
    public Integer deleteEmploy(Integer id);

    //修改员工
    public Integer updateEmploy(Employee employee);

    //通过用户名获取用户id
    public Long selectIdByUserName(String username);


    //通过员工id查找员工的所有信息
    public Employee selectEmployeeById(Long id);
}
