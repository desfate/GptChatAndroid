package com.github.desfate.gptcore.chat;

public interface ChatEngine {

    /**
     * 发送消息接口
     * @param message
     * @return 返回消息
     */
    public <T> String sendMessageNet(T message);


}
