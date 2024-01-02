package cn.litteleterry.rpc.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.util.Assert;

public class RedisRpcTopicRegisterEvent extends ApplicationEvent {
    private final PatternTopic patternTopic;

    public RedisRpcTopicRegisterEvent(Object source, PatternTopic patternTopic) {
        super(source);
        Assert.notNull(patternTopic, "ChangeSet must not be null");
        this.patternTopic = patternTopic;
    }


    public PatternTopic getPatternTopic() {
        return this.patternTopic;
    }
}
