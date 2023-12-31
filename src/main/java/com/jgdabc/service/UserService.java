package com.jgdabc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jgdabc.entity.User;

/**
 * @Description: 用户业务层接口
 * @Date: Created in 22:56 2020/5/26
 * @Author: ONESTAR
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
public interface UserService extends IService<User> {
    //核对用户名和密码
    User checkUser(String username, String password);
}