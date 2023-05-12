package cn.litteleterry.rpc;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.cglib.proxy.InvocationHandler;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RpcRemoteClientProxyBean<T> implements FactoryBean<T>, InvocationHandler {
    private Class<?> interfaceType;

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Class<?> interfaceType) {
        this.interfaceType = interfaceType;
    }

    public T getObject() {
        return (T)Proxy.newProxyInstance(interfaceType.getClassLoader(),new Class[]{interfaceType},this::invoke);
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) {
        RRemoteClient anno = this.interfaceType.getAnnotationsByType(RRemoteClient.class)[0];
        String methodName = method.getName();
        RedisTemplate<String,String> redisTemplate = ApplicationContextAwareHolder.getBean(StringRedisTemplate.class);
        String requestKey = generateServiceName(this.interfaceType.getName(),methodName);
        ServiceTopicMessage topicMessage = new ServiceTopicMessage(anno.value(),methodName,requestKey,objects);
        redisTemplate.opsForList().leftPush("REDIS_RPC", JSON.toJSONString(topicMessage));
        CompletableFuture<String> completableFuture = new CompletableFuture();
        waitRpcResult(completableFuture,requestKey);
        try {
            return completableFuture.get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }return null;
    }

    public String generateServiceName(String clsName,String methodName){
        return clsName+methodName;
    }

    public void waitRpcResult(CompletableFuture<String> completableFuture,String requestKey){
        RedisTemplate<String,String> redisTemplate = ApplicationContextAwareHolder.getBean(StringRedisTemplate.class);
        CompletableFuture.supplyAsync(() -> {
            String result = redisTemplate.opsForValue().get(requestKey);
            while (Objects.isNull(result)){
                result = redisTemplate.opsForValue().get(requestKey);
            }
            completableFuture.complete(result);
            return result;
        });
    }
}
