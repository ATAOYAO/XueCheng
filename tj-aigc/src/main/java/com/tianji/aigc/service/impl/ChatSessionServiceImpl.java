package com.tianji.aigc.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tianji.aigc.config.SessionProperties;
import com.tianji.aigc.entity.ChatSession;
import com.tianji.aigc.mapper.ChatSessionMapper;
import com.tianji.aigc.service.ChatSessionService;
import com.tianji.aigc.vo.SessionVO;
import com.tianji.common.utils.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatSessionServiceImpl extends ServiceImpl<ChatSessionMapper, ChatSession> implements ChatSessionService {

    private final SessionProperties sessionProperties;

    @Override
    public SessionVO createSession(Integer n) {
        // 获取热门问题内容
        SessionVO vo = BeanUtil.toBean(sessionProperties, SessionVO.class);
        // 根据n随机获取examples
        vo.setExamples(RandomUtil.randomEleList(sessionProperties.getExamples(),n));
        // 生成唯一的sessionid
        vo.setSessionId(IdUtil.simpleUUID());
        // 新建会话内容保存到数据库中
        ChatSession chatSession = new ChatSession();
        chatSession.setUserId(UserContext.getUser());
        chatSession.setSessionId(vo.getSessionId());
        save(chatSession);
        return vo;

    }

    @Override
    public List<SessionVO.Example> hotExamples(Integer n) {
        return RandomUtil.randomEleList(sessionProperties.getExamples(),n);
    }
}
