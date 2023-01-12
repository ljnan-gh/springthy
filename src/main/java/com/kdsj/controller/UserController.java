package com.kdsj.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kdsj.common.Grid;
import com.kdsj.entity.User;
import com.kdsj.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("manager")
    public String index() {

        return "user/userList";
    }

    @RequestMapping("datagrid")
    @ResponseBody
    public Object datagrid(Integer page, Integer rows) {
        Page<User> p = new Page<User>(page, rows);
        IPage<User> userList = userService.page(p);
        return new Grid(userList.getRecords(), userList.getTotal());
    }

}
