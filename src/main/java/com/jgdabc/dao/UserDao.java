package com.jgdabc.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jgdabc.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
//这里改造为mybatisplus框架了

@Mapper
@Repository
public interface UserDao extends BaseMapper<User> {

    User findByUserNameAndEmail(@Param("username") String username,@Param("email") String email);
    User findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
//    void InsertUser(User user);



}