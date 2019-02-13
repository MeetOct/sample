package com.meet.sample.config;

import com.meet.sample.controller.MessageController;
import com.meet.sample.message.MessageScanner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigServiceAutoConfiguration {

    @Configuration
    static class MessageScannerConfiguration {
        private final MessageController messageController;

        MessageScannerConfiguration(MessageController messageController) {
            this.messageController = messageController;
        }

        @Bean
        public MessageScanner releaseMessageScanner() {
            MessageScanner messageScanner=new MessageScanner();
            messageScanner.addMessageListener(messageController);
            return messageScanner;
        }

    }
}
