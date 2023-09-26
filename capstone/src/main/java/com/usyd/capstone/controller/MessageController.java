package com.usyd.capstone.controller;


import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.MessageDB;
import com.usyd.capstone.entity.VO.GetMatchUserInfo;
import com.usyd.capstone.entity.VO.GetMessageHistory;
import com.usyd.capstone.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/chatRoom/getMessageHistory")
    public Result getMessageHistory(@RequestBody GetMessageHistory getMessageHistory){
        List<MessageDB> messagelist =  messageService.getMessageHistory(getMessageHistory.getEmail1(), getMessageHistory.getEmail2());
      return Result.suc(messagelist);
    }

    @PostMapping("/chatRoom/getMatchUserInfo")
    public Result getMatchUserInfo(@RequestBody GetMatchUserInfo getMatchUserInfo){

        return messageService.getMatchUserInfo(getMatchUserInfo);

    }



}

