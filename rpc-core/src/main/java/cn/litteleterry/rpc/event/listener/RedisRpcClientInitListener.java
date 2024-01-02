package cn.litteleterry.rpc.event.listener;

import cn.litteleterry.rpc.ApplicationContextAwareHolder;
import cn.litteleterry.rpc.event.RedisRpcTopicRegisterEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisRpcClientInitListener implements ApplicationListener<ContextRefreshedEvent> {

    public void init() {
        PatternTopic patternTopic = new PatternTopic("22222");
        ApplicationContextAwareHolder.publishEvent(new RedisRpcTopicRegisterEvent(patternTopic, patternTopic));
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.init();
    }
}
