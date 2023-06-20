package com.jgdabc.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jgdabc.annotation.AccessLimit;
import com.jgdabc.entity.Comment;
import com.jgdabc.entity.User;
import com.jgdabc.queryvo.DetailedBlog;
import com.jgdabc.service.BlogService;
import com.jgdabc.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Description: 评论控制器
 * @Date: Created in 10:25 2022/12/22
 * @Author: 兰舟千帆

 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    //查询评论列表
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        List<Comment> comments = commentService.listCommentByBlogId(blogId);

        model.addAttribute("comments", comments);

        return "blog :: commentList";
    }

    //新增评论
    @PostMapping("/comments")
//    这里加入了访问控制
    @AccessLimit(seconds = 15, maxCount = 6,needLogin = true) //15秒内 允许请求6次
    public String post(Comment comment, HttpSession session, Model model, RedirectAttributes attributes, HttpServletRequest request) {
        Long blogId = comment.getBlogId();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
            comment.setNickname(user.getUsername());
        } else {
            if (session.getAttribute("user_")==null)
            {
//              执行逻辑
            }
            if (StringUtils.isEmpty(comment.getNickname()))
            {
                Object user_ = session.getAttribute("user_");
                if (user_==null)
                {
                    comment.setNickname("游客");
                }
                else {
                    comment.setNickname((String) session.getAttribute("user_"));
                }

                comment.setAvatar(avatar);
            }
            //设置默认头像

        }
        Long parentId = comment.getParentComment().getId();
        Comment parentComment = null;
        if (comment.getParentComment().getId() != null) {
            comment.setParentCommentId(parentId);

            // 根据父评论id查询留言信息
            parentComment = commentService.getEmailByParentId(parentId);
        }
        commentService.saveComment(comment,parentComment);
        List<Comment> comments = commentService.listCommentByBlogId(blogId);

        model.addAttribute("comments", comments);
        return "blog :: commentList";
    }

    //删除评论
    @GetMapping("/comment/{blogId}/{id}/delete")
    public String delete(@PathVariable Long blogId, @PathVariable Long id, Comment comment, HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        if(user != null) {
            commentService.deleteComment(comment,id);
        }
        DetailedBlog detailedBlog = blogService.getDetailedBlog(blogId);
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("blog", detailedBlog);
        model.addAttribute("comments", comments);
        return "blog";
    }

}
