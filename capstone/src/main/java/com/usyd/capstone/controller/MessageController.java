package com.usyd.capstone.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.MessageDB;
import com.usyd.capstone.entity.Notification;
import com.usyd.capstone.entity.VO.GetMatchUserInfo;
import com.usyd.capstone.entity.VO.GetMessageHistory;
import com.usyd.capstone.service.MessageService;
import com.usyd.capstone.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author yyf
 * @since 2023年09月02日
 * member接口不接受public的访问
 */
@RestController
@RequestMapping("/member")
public class MessageController {
    @Autowired
    MessageService messageService;

    @Autowired
    NotificationService notificationService;

    @PostMapping("/chatRoom/getMessageHistory")
    public Result getMessageHistory(@RequestBody GetMessageHistory getMessageHistory){
        List<MessageDB> messagelist =  messageService.getMessageHistory(getMessageHistory.getEmail1(), getMessageHistory.getEmail2());
      return Result.suc(messagelist);
    }

    @PostMapping("/chatRoom/getMatchUserInfo")
    public Result getMatchUserInfo(@RequestBody GetMatchUserInfo getMatchUserInfo){

        return messageService.getMatchUserInfo(getMatchUserInfo);

    }


    @GetMapping("/notification/getUnreadNotification")
    public Result getUnreadNotification(@RequestParam Integer userId){

        List<Notification> notificationList = notificationService.list(
          new QueryWrapper<Notification>()
                  .eq("isRead", 0)
                  .eq("receivedUserId", userId)
        );
        return Result.suc(notificationList);

    }

    @GetMapping("/notification/getAllNotification")
    public Result getAllNotification(@RequestParam Integer userId){

        List<Notification> notificationList = notificationService.list(
                new QueryWrapper<Notification>()
                        .eq("receivedUserId", userId)
                        .orderByDesc("sendTime")
        );
        return Result.suc(notificationList);

    }

    @GetMapping("/notification/readNotification")
    public Result readNotification(@RequestParam Integer notificationId, @RequestParam Integer isRead){

     Notification notification = notificationService.getById(notificationId);
     notification.setIsRead(isRead);
     notificationService.saveOrUpdate(notification);
        return Result.suc();

    }

    @GetMapping("/notification/deleteNotification")
    public Result readNotification(@RequestParam Integer notificationId){

        notificationService.removeById(notificationId);

        return Result.suc();

    }


}

