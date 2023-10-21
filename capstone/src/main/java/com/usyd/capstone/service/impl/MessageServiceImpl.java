package com.usyd.capstone.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.MessageDB;
import com.usyd.capstone.entity.VO.GetMatchUserInfo;
import com.usyd.capstone.mapper.MessageMapper;
import com.usyd.capstone.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yyf
 * @since 2023年09月02日
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, MessageDB> implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Override
    public  List<MessageDB> getMessageHistory(String email1, String email2) {

        List<MessageDB> messageDBList = messageMapper.selectAllRecordByTwoEmail(email1, email2);
        return messageDBList;
    }

    @Override
    public Result getMatchUserInfo(GetMatchUserInfo getMatchUserInfo) {

        if (getMatchUserInfo.getRole().equals("employer")){
            // search all match tasker info
           return Result.suc(messageMapper.getMatchTaskerInfo(getMatchUserInfo.getUserId()));
        } else if (getMatchUserInfo.getRole().equals("labor")) {
            // search all match employer info
          return Result.suc(messageMapper.getMatchEmployerInfo(getMatchUserInfo.getUserId()));
        }
        return Result.fail();
    }
}
