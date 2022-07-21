package com.kuang.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuang.reggie.common.BaseContext;
import com.kuang.reggie.entity.Employee;
import com.kuang.reggie.mapper.EmployeeMapper;
import com.kuang.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
@Slf4j
@Service
@Transactional
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;

    @Override
    public Employee login(String username, String password) {
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        employeeQueryWrapper.eq("username", username);
        employeeQueryWrapper.eq("password", md5Password);
        Employee employee = employeeMapper.selectOne(employeeQueryWrapper);
        return employee;
    }

    @Override
    public IPage selectEmployeeByPage(Page page, String name) {
        if(name==null || name.equals("")){
            return employeeMapper.selectPage(page, null);
        }
        String names = name.trim();
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.like("name", names);
        return employeeMapper.selectPage(page, employeeQueryWrapper);

    }


    //添加员工
    @Override
    public Integer addEmploy(Employee employee) {

        log.info("添加员工的threadLocal为"+ BaseContext.getCurrent());
        return employeeMapper.insert(employee);

    }

    //删除员工
    @Override
    public Integer deleteEmploy(Integer id) {
        return employeeMapper.deleteById(id);
    }

    //修改员工
    @Override
    public Integer updateEmploy(Employee employee) {
        return employeeMapper.updateById(employee);
    }

    //通过用户名获取员工id
    @Override
    public Long selectIdByUserName(String username) {
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("username", username);
        Employee employee = employeeMapper.selectOne(employeeQueryWrapper);
        return employee.getId();
    }

    //通过员工id查找员工信息
    @Override
    public Employee selectEmployeeById(Long id) {
        QueryWrapper<Employee> employeeQueryWrapper = new QueryWrapper<>();
        employeeQueryWrapper.eq("id",id);
        Employee employee = employeeMapper.selectOne(employeeQueryWrapper);
        return employee;
    }
}
