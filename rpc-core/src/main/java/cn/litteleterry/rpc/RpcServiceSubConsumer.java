package cn.litteleterry.rpc;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class RpcServiceSubConsumer implements InitializingBean {
    private static final Logger log = LoggerFactory.getLogger(RedisRpcGet.class);

    @Resource
    RpcConsumerHandler consumerHandler;

    @Override
    public void afterPropertiesSet() {
        CompletableFuture.runAsync(()->{
            listenTopic();
        });
    }

    private void listenTopic()  {
        while (true) {
            RedisTemplate<String,String> redisTemplate = RedisTemplateUtil.getRedisTemplate();
            String topic = redisTemplate.opsForList().rightPop("helloService");
            if (Objects.nonNull(topic)){
                log.info("Listening to message records：====>" +topic);
                RpcTopicMessage topicMessage = JSON.parseObject(topic, RpcTopicMessage.class);
                consumerHandler.handlerRecord(topicMessage);
            }
        }
    }
}
