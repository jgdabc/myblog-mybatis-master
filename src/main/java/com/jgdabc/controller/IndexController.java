package com.jgdabc.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jgdabc.common.BaseContext;
import com.jgdabc.common.ExceptionHander.CustomException;
import com.jgdabc.common.R_;
import com.jgdabc.entity.Comment;
import com.jgdabc.entity.User;
import com.jgdabc.queryvo.DetailedBlog;
import com.jgdabc.queryvo.FirstPageBlog;
import com.jgdabc.queryvo.NewComment;
import com.jgdabc.queryvo.RecommendBlog;
import com.jgdabc.service.BlogService;
import com.jgdabc.service.CommentService;
import com.jgdabc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * @Description: 首页控制器
 * @Date: Created in 21:01 2022/8/20
 * @Author: 兰舟千帆

 */
@Slf4j
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @GetMapping("/verification.html")
    public String toVerfication()
    {
        return "verification";
    }


    @GetMapping("tologin")
    public String tologin(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, RedirectAttributes attributes)
    {
        PageHelper.startPage(pageNum,10);
        //查询博客列表
        List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
        //查询最新推荐博客
        List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
        //查询最新评论
        List<NewComment> newComments = blogService.getNewComment();

        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("recommendedBlogs", recommendedBlog);
        model.addAttribute("newComment",newComments);
        return "index";
    }
    @GetMapping("returnLogin")
    public String returnLogin()
    {
        return "login";
    }




    //分页查询博客列表
    @GetMapping("/")
    public String index(Model model, @RequestParam(defaultValue = "1",value = "pageNum") Integer pageNum, RedirectAttributes attributes) {

        PageHelper.startPage(pageNum,10);
        //查询博客列表
        List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
        //查询最新推荐博客
        List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
        //查询最新评论
        List<NewComment> newComments = blogService.getNewComment();

        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("recommendedBlogs", recommendedBlog);
        model.addAttribute("newComment",newComments);
        return "index";


    }
    @RequestMapping("login/getcode")
    public void getCode(HttpServletResponse response, HttpSession session) throws IOException {
        //    生成验证码
//    生成验证码定义长宽高
        session.setMaxInactiveInterval(120);//设置session的存活时间为两分钟
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(116, 34, 4, 10);
//        将验证码存放到session里面
        session.setAttribute("code",lineCaptcha.getCode());
//        将验证码输出来
        ServletOutputStream outputStream = response.getOutputStream();
        lineCaptcha.write(outputStream);
        outputStream.close();//关闭流


    }
//    注册功能
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    @ResponseBody
    public R_<String>register(@RequestBody User user)
    {
       String email = user.getEmail();
        QueryWrapper<User> userQueryWrapperUserName = new QueryWrapper<>();
        QueryWrapper<User> userQueryWrapperEmail = new QueryWrapper<>();
//        userQueryWrapperAccount.eq("account",account);
        userQueryWrapperUserName.eq("username",user.getUsername());
        User userByUserName = userService.getOne(userQueryWrapperUserName);
        userQueryWrapperEmail.eq("email",email);
        User userByEmail = userService.getOne(userQueryWrapperEmail);

        if (userByUserName!=null)
        {
            return R_.error("用户"+userByUserName+"已经存在");
        }
        if (userByEmail!=null)
        {
            return R_.error("邮箱"+email+"已经注册");
        }

        String password = user.getPassword();
        user.setNickname(user.getUsername());

        System.out.println("获取到注册请求数据");
        System.out.println("注册请求数据->"+user);
        boolean save = userService.save(user);
        if (save)
        {
            return R_.success("注册成功");
        }


        throw  new CustomException("数据未知异常");





    }
//    编写登录的Controller
    /**
     * 下面这里定义的POST其实可有不写，这里其实就是说明前端请求了一个Post，当然其实这里直接Request
     * 也可以的，只是后台如果提交request是不太行的，这样其实也是很标准的一套写法
     * @param user
     * @return
     */
    @RequestMapping(value = "/loginInFo",method = RequestMethod.POST)
    @ResponseBody
    public R_<String> loginInFo(@RequestBody  User user,HttpSession session )
    {
//        执行登录功能
        log.info("执行登录的功能");
        String  sessionCode = (String) session.getAttribute("code");//获取到session中存储的数据
        if (sessionCode==null)
        {
            throw  new CustomException("验证码已经失效，请点击失效");
        }
        String codeVerify = user.getCodeVerify();//获取前台提交过来的验证码
        if (!sessionCode.equals(codeVerify))
        {
            return  R_.error("验证码错误");

        }
        String username = user.getUsername();
        String password = user.getPassword();
        User user1 = userService.checkUser(username, password);
        if (user1!=null)
        {
////           将用户信息放到session里面吧，因为后面可能用到
//            session.setAttribute("user",user.getUsername());
//            session.setAttribute("nickname",user.getNickname());
            session.setAttribute("user_",user.getUsername());
            session.setMaxInactiveInterval(-1);//设置无限时长
//            BaseContext.setCurrentUser(username);
            return  R_.success("恭喜您成功登录");
        }
        else if (user1== null)
        {
            return  R_.error("账户或密码错误");
        }
        throw  new CustomException("数据出现未知异常");



    }





    //搜索博客
    @PostMapping("/search")
    public String search(Model model,
                         @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
                         @RequestParam String query) {
        PageHelper.startPage(pageNum, 1000);
        List<FirstPageBlog> searchBlog = blogService.getSearchBlog(query);

        PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(searchBlog);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("query", query);
        return "search";
    }

    //博客信息
    @GetMapping("/footer/blogmessage")
    public String blogMessage(Model model){
        int blogTotal = blogService.getBlogTotal();
        int blogViewTotal = blogService.getBlogViewTotal();
        int blogCommentTotal = blogService.getBlogCommentTotal();
        int blogMessageTotal = blogService.getBlogMessageTotal();

        model.addAttribute("blogTotal",blogTotal);
        model.addAttribute("blogViewTotal",blogViewTotal);
        model.addAttribute("blogCommentTotal",blogCommentTotal);
        model.addAttribute("blogMessageTotal",blogMessageTotal);
        return "index :: blogMessage";
    }

    //跳转博客详情页面
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model) {
        DetailedBlog detailedBlog = blogService.getDetailedBlog(id);
        List<Comment> comments = commentService.listCommentByBlogId(id);
        model.addAttribute("comments", comments);
        model.addAttribute("blog", detailedBlog);
        return "blog";
    }


}