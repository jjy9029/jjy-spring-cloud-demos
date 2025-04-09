package com.jjy.mybatisPlus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jjy.mybatisPlus.domain.User;
import com.jjy.mybatisPlus.mapper.UserMapper;
import com.jjy.mybatisPlus.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
