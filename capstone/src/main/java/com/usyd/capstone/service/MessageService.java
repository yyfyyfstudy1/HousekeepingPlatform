package com.usyd.capstone.service;

import com.usyd.capstone.common.util.Result;
import com.usyd.capstone.entity.MessageDB;
import com.baomidou.mybatisplus.extension.service.IService;
import com.usyd.capstone.entity.VO.GetMatchUserInfo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yyf
 * @since 2023年09月02日
 */
public interface MessageService extends IService<MessageDB> {


    List<MessageDB> getMessageHistory(String email1, String email2);

    Result getMatchUserInfo(GetMatchUserInfo getMatchUserInfo);
}
