package com.github.desfate.gptcore.chat;

public class GptCoreEngine implements ChatEngine{

    @Override
    public <T> String sendMessageNet(T message) {
        if (message instanceof String){  // 发送的是文本消息

        }
        return null;
    }


}
