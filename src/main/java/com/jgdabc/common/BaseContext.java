package com.jgdabc.common;
//基于ThreadLocal封装的工具类，用于保存获取用户id
public class BaseContext {

    private static ThreadLocal<String> threadLocal = new ThreadLocal<>();
    public static void setCurrentUser(String userName)
    {
        threadLocal.set(userName);
    }
    public static String getCurrentUser()
    {
        return  threadLocal.get();
    }
}
