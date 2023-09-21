package com.usyd.capstone.mapper;

import com.usyd.capstone.entity.MessageDB;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.usyd.capstone.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yyf
 * @since 2023年09月02日
 */
@Mapper
public interface MessageMapper extends BaseMapper<MessageDB> {

    List<MessageDB> selectAllRecordByTwoEmail(String email1, String email2);

    List<User> getMatchTaskerInfo(Integer userId);

    List<User> getMatchEmployerInfo(Integer userId);
}
