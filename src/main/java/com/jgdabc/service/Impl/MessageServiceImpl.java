package com.jgdabc.service.Impl;

import com.jgdabc.dao.MessageDao;
import com.jgdabc.entity.Message;
import com.jgdabc.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**

 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDao messageDao;

    // 自动导入Java邮件发送实现类
//    这里自动装配可在代码运行加载完成后成功
    @Autowired
    private JavaMailSender javaMailSender;

    //存放迭代找出的所有子代的集合
    private List<Message> tempReplys = new ArrayList<>();

    /**
     * @Description: 查询留言
     * @Auther: ONESTAR
     * @Date: 17:26 2020/4/14
     * @Param:
     * @Return: 留言消息
     */
    @Override
    //这里有缓存，所以有时候删除重新查看的时候原来的留言还在，过一段时间就消失了
//     @Cacheable(value = "messageList",key = "'message'")
    public List<Message> listMessage() {
        //查询出父节点
        List<Message> messages = messageDao.findByParentIdNull(Long.parseLong("-1"));
        for(Message message : messages){
            Long id = message.getId();
            String parentNickname1 = message.getNickname();
            List<Message> childMessages = messageDao.findByParentIdNotNull(id);

            //查询出子留言
            combineChildren(childMessages, parentNickname1);
            message.setReplyMessages(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return messages;
    }

    /**
     * @Description: 查询出子留言
     * @Auther: ONESTAR
     * @Date: 17:31 2020/4/14
     * @Param: childMessages：所有子留言
     * @Param: parentNickname1：父留言的姓名
     * @Return:
     */
    private void combineChildren(List<Message> childMessages, String parentNickname1) {
        //判断是否有一级子回复
        if(childMessages.size() > 0){
            //循环找出子留言的id
            for(Message childMessage : childMessages){
                String parentNickname = childMessage.getNickname();
                childMessage.setParentNickname(parentNickname1);
                tempReplys.add(childMessage);
                Long childId = childMessage.getId();
                //查询二级以及所有子集回复
                recursively(childId, parentNickname);
            }
        }
    }

    /**
     * @Description: 循环迭代找出子集回复
     * @Auther: ONESTAR
     * @Date: 17:33 2020/4/14
     * @Param: childId：子留言的id
     * @Param: parentNickname1：子留言的姓名
     * @Return:
     */
    private void recursively(Long childId, String parentNickname1) {
        //根据子一级留言的id找到子二级留言
        List<Message> replayMessages = messageDao.findByReplayId(childId);

        if(replayMessages.size() > 0){
            for(Message replayMessage : replayMessages){
                String parentNickname = replayMessage.getNickname();
                replayMessage.setParentNickname(parentNickname1);
                Long replayId = replayMessage.getId();
                tempReplys.add(replayMessage);
                //循环迭代找出子集回复
                recursively(replayId,parentNickname);
            }
        }
    }

    @Override
    //存储留言信息
    public int saveMessage(Message message,Message parentMessage) {
        message.setCreateTime(new Date());

        // 判断是否有父评论，有的话就发送邮件
        if(!StringUtils.isEmpty(parentMessage)){


                String parentNickname = parentMessage.getNickname();
                String nickName = message.getNickname();
                String comtent = "亲爱的" + parentNickname + "，您在【兰舟千帆博客】的评论收到了来自" + nickName + "的回复！内容如下：" + "\r\n" + "\r\n" +  message.getContent();
                String parentEmail = parentMessage.getEmail();
                String nickname = message.getNickname();

                // 发送邮件
                SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                simpleMailMessage.setSubject(nickname+"回复你了");  //主题
                simpleMailMessage.setText(comtent);   //内容
                simpleMailMessage.setTo(parentEmail); //接收者的邮箱
                if (!StringUtils.isEmpty(parentEmail)) //先判断父级别邮箱
                {
                    if (!StringUtils.isEmpty(message.getEmail())) //然后判断回复者邮箱
                    {
                        simpleMailMessage.setFrom("2121325875@qq.com");//发送者邮箱
                        javaMailSender.send(simpleMailMessage);
                    }

                }





        }

        return messageDao.saveMessage(message);
    }

//    删除留言
    @Override
    public void deleteMessage(Long id) {
        messageDao.deleteMessage(id);
    }

    @Override
    public Message getEmailByParentId(Long parentId) {
        return messageDao.getEmailByParentId(parentId);
    }
}