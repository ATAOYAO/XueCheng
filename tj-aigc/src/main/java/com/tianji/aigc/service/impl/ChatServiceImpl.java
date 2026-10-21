package com.tianji.aigc.service.impl;

import cn.hutool.core.date.DateUtil;
import com.tianji.aigc.config.SystemPromptConfig;
import com.tianji.aigc.dto.ChatDTO;
import com.tianji.aigc.enums.ChatEventTypeEnum;
import com.tianji.aigc.service.ChatService;
import com.tianji.aigc.vo.ChatEventVO;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;

    private final SystemPromptConfig systemPromptConfig;

    @Override
    public Flux<ChatEventVO> chat(ChatDTO chatDTO) {
        String sessionId = chatDTO.getSessionId();
        String question = chatDTO.getQuestion();
        return chatClient.prompt()
                // 设置系统提示词
                .system(promptSystemSpec -> promptSystemSpec
                        // 读取配置中的提示词
                        .text(systemPromptConfig.getChatSystemMessage().get())
                        // 设置当前时间参数
                        .param("now", DateUtil.now())
                )
                // 用户输入的问题
                .user(question)
                .stream()
                // 大模型返回的内容
                .chatResponse()
                // 将大模型返回的内容转化为自定义类
                .map(chatResponse -> {
                    ChatEventVO chatEventVO = new ChatEventVO();
                    chatEventVO.setEventData(chatResponse.getResult().getOutput().getText());
                    chatEventVO.setEventType(ChatEventTypeEnum.DATA.getValue());
                    return chatEventVO;
                })
                // 标记结束输出
                .concatWith(Flux.just(ChatEventVO.builder()
                        .eventType(ChatEventTypeEnum.STOP.getValue())
                        .build()));
    }
}
