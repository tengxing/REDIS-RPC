package cn.litteleterry.rpc;

import cn.litteleterry.rpc.util.R;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class RpcServiceSubConsumer implements InitializingBean {
    private static List<String> TOPIC_NAMES = Arrays.asList(new String[]{"HelloService"});

    @Autowired

    @Override
    public void afterPropertiesSet() throws Exception {
        CompletableFuture.runAsync(()->{
            listenTopic();
        });
    }

    private void listenTopic()  {
        while (true) {
            RedisTemplate<String,String> redisTemplate = ApplicationContextAwareHolder.getBean(StringRedisTemplate.class);
            String topic = redisTemplate.opsForList().rightPop("REDIS_RPC");
            if (Objects.nonNull(topic)){
                System.out.println("监听到消息：====>" +topic);
                CompletableFuture.runAsync(()-> {
                    ServiceTopicMessage topicMessage = JSON.parseObject(topic, ServiceTopicMessage.class);
                    String serviceName = topicMessage.getServiceName();
                    String serviceMethod = topicMessage.getMethodName();

                    Object objectService = ApplicationContextAwareHolder.getBean(serviceName);
                    Object[] requestParamers = topicMessage.getArgs();

                    R r = new R();
                    try {
                        Method method;
                        if (Objects.nonNull(requestParamers) && requestParamers.length>0){
                            Class[] clazzs=new Class[requestParamers.length];
                            for(int i=0;i<requestParamers.length;i++){
                                clazzs[i]=requestParamers[i].getClass();
                            }
                            method = objectService.getClass().getMethod(serviceMethod,clazzs);
                        }else {
                            method = objectService.getClass().getMethod(serviceMethod);
                        }
                        Object result = method.invoke(objectService, requestParamers);
                        System.out.println("method.invoke=======>" + result);
                        r.setCode(10000);
                        r.setData(result);
                    } catch (Exception e) {
                        System.out.println(e.getCause());
                        e.printStackTrace();
                        r.setCode(20001);
                        r.setData(e.getCause());
                    }
                    redisTemplate.opsForValue().set(topicMessage.getRequestKey(), JSON.toJSONString(r));
                });
            }
        }
    }
}
