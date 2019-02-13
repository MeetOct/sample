package com.meet.sample.message;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MessageScanner implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MessageScanner.class);
    private List<MessageListener> listeners;
    private ScheduledExecutorService executorService;

    public MessageScanner() {
        listeners=Lists.newCopyOnWriteArrayList();
        executorService = Executors.newScheduledThreadPool(1);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService.scheduleWithFixedDelay(()->{
            try {
                scanMessages();
            }catch (Throwable ex){

            }
            finally {

            }
            },
                500,500, TimeUnit.MILLISECONDS);
    }

    /**
     * 扫描信息
     */
    private void  scanMessages(){
        boolean hasMoreMessages = true;
        while (hasMoreMessages && !Thread.currentThread().isInterrupted()) {
            hasMoreMessages = scanAndSendMessages();
        }
    }

    public void addMessageListener(MessageListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    private boolean scanAndSendMessages() {
        List<ReleaseMessage> releaseMessages=Lists.newCopyOnWriteArrayList();
        //redis 数据变更通知
        if(true){
            ReleaseMessage message=new ReleaseMessage();
            message.setKey("1");
            message.setMessage("test1");

            ReleaseMessage message2=new ReleaseMessage();
            message2.setKey("2");
            message2.setMessage("test2");

            releaseMessages.add(message);
            releaseMessages.add(message2);
        }

        if (CollectionUtils.isEmpty(releaseMessages)) {
            return false;
        }
        fireMessageScanned(releaseMessages);
        return false;
    }

    private void fireMessageScanned(List<ReleaseMessage> messages) {

        for (ReleaseMessage message : messages) {
            for (MessageListener listener : listeners) {
                try {
                    listener.handleMessage(message);
                } catch (Throwable ex) {
                    logger.error("Failed to invoke message listener {}", listener.getClass(), ex);
                }
            }
        }
    }
}
