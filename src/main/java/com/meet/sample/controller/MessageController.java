package com.meet.sample.controller;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.meet.sample.message.MessageListener;
import com.meet.sample.message.MessageScanner;
import com.meet.sample.message.ReleaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;

@RestController
@RequestMapping("/message/notification")
public class MessageController implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    private final Multimap<String, DeferredResult> deferredResults = Multimaps.synchronizedSetMultimap(HashMultimap.create());
    List<String> keys=Lists.newCopyOnWriteArrayList();
    public MessageController(){
        keys.add("1");
        keys.add("2");
    }

    @GetMapping
    public DeferredResult<ResponseEntity<String>> pollNotification(@RequestParam(value = "id") String id){
        DeferredResult<ResponseEntity<String>> result=new DeferredResult(Long.valueOf(60 * 1000),new ResponseEntity<String>(HttpStatus.NOT_MODIFIED));

        result.onTimeout(()-> logger.error("pollNotification time out"));

        result.onCompletion(()->{
            deferredResults.remove(id,result);
            logger.info("pollNotification completion");
        });
        deferredResults.put(id,result);
        return result;
    }

    @Override
    public void handleMessage(ReleaseMessage message) {
//        logger.info("message received - message: {}", message);

        String key=message.getKey();
        //create a new list to avoid ConcurrentModificationException
        List<DeferredResult> results = Lists.newArrayList(deferredResults.get(key));
        if (CollectionUtils.isEmpty(results)) {
            return;
        }
        for (DeferredResult result : results) {
            result.setResult(new ResponseEntity<>(message.getMessage(),HttpStatus.OK));
        }
        logger.info("Notification completed");
    }
}
