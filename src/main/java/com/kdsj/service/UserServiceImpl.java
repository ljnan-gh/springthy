package com.kdsj.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kdsj.entity.User;
import com.kdsj.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
