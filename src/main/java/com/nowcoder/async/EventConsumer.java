package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author plancer16
 * @create 2021/5/13 15:54
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    private JedisAdapter jedisAdapter;

    private Map<EventType, List<EventHandler>> config = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans!=null){
            for (Map.Entry<String, EventHandler> entry : beans.entrySet()) {
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();

                for (EventType eventType : eventTypes) {
                    if (!config.containsKey(eventType)) {
                        config.put(eventType, new ArrayList<EventHandler>());
                    }
                    config.get(eventType).add(entry.getValue());
                }
            }
        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String key = RedisKeyUtil.getEventQueueKey();
                List<String> events = jedisAdapter.brpop(0, key);

                for (String message : events) {
                    if (message.equals(key)) {
                        continue;
                    }
                    EventModel eventModel = JSONObject.parseObject(message, EventModel.class);
                    if (!config.containsKey(eventModel.getType())) {
                        logger.info("?????????????????????");
                        continue;
                    }
                    for (EventHandler eventHandler : config.get(eventModel.getType())) {
                        eventHandler.doHandler(eventModel);//?????????events??????????????????eventType??????handler???????????????eventModel
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
