package cn.litteleterry.rpc;

import cn.litteleterry.rpc.util.R;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
public class RpcConsumerHandlerImpl implements RpcConsumerHandler{
    private static final Logger log = LoggerFactory.getLogger(RpcConsumerHandlerImpl.class);

    @SuppressWarnings({ "unchecked", "unused", "rawtypes" })
    @Override
    public void handlerRecord(RpcTopicMessage topicMessage) {
        CompletableFuture.runAsync(()-> {
            String serviceName = topicMessage.getServiceName();
            String serviceMethod = topicMessage.getMethodName();
            String resultKey = RpcContants.getVId(topicMessage.getUuid());

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
            RedisTemplateUtil.getRedisTemplate().opsForValue().set(resultKey, JSON.toJSONString(r),100000, TimeUnit.MILLISECONDS);
            log.info("RpcServiceSubConsumer write remote result from the redisRpc...." +
                    "\nresult key【{}】" +
                    "\nresult content【{}】",resultKey, JSON.toJSONString(r));
        });
    }
}
