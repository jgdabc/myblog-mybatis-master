package com.jgdabc.controller.admin;

import com.jgdabc.common.R_;
import com.jgdabc.entity.User;
import com.jgdabc.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @Description: 用户登录控制器
 * @Date: Created in 21:40 2022/11/2
 * @Author: 兰舟千帆

 */
@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * @Description: 跳转登录页面
     * @Auther: 兰舟千帆
     * @Date: 21:57 2022/11/2
     * @Param:
     * @Return: 返回登录页面
     */
    @GetMapping
    public String loginPage(){
        System.out.println("执行管理员登录...");

        return "admin/login";
    }




    /**
     * @Description: 登录校验
     * @Auther: ONESTAR
     * @Date: 10:04 2022/5/27
     * @Param: username:用户名
     * @Param: password:密码
     * @Param: session:session域
     * @Param: attributes:返回页面消息
     * @Return: 登录成功跳转登录成功页面，登录失败返回登录页面
     */
//    @PostMapping("/login")
//
//    public String login(@RequestBody User user,HttpSession session, RedirectAttributes attributes)
//    {
//        String username = user.getUsername();
//        String password = user.getPassword();
//        User user_check = userService.checkUser(username, password);
//        if (user_check!=null)
//        {
//            user.setPassword(null);
//            session.setAttribute("user",user);
//            session.setMaxInactiveInterval(-1);
//            return "admin/index";
//
//        }else {
//            attributes.addFlashAttribute("message", "用户名和密码错误");
//            return "redirect:/admin";
//        }



//
//    }
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes) {
///我这里首先将我的密码进行了一次md5加密，然后存入数据库，然后登录的时候再次对登录的密码进行md5加密，这样比对的时候
//        就是对两次加密的md5密码进行比对，这样其实是比较安全了。由于md5不可解密，所以数据库密码很难被破解。
        String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
        User user = userService.checkUser(username,md5Password);


        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user",user);
            session.setMaxInactiveInterval(-1);     // 设置session永不过期
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");
            return "redirect:/admin";
        }
    }
//    @PostMapping("/forget_login")
//    public String forget_login(@RequestParam String )
//    {
//
//    }

    /**
     * @Description: 注销
     * @Auther: 兰舟千帆
     * @Date: 10:15 2022/5/27
     * @Param: session:session域
     * @Return: 返回登录页面
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}