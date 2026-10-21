package com.tianji.aigc.controller;

import com.tianji.aigc.dto.ChatDTO;
import com.tianji.aigc.service.ChatService;
import com.tianji.aigc.vo.ChatEventVO;
import com.tianji.common.annotations.NoWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;


    /*
    * 流式对话
    * */
    @NoWrapper
    @PostMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatEventVO> chat(@RequestBody ChatDTO chatDTO){
        return chatService.chat(chatDTO);
    }
}
