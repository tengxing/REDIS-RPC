package cn.litteleterry.rpc;

import cn.litteleterry.rpc.util.R;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class RedisRpcGet implements Callable<R> {
    private static final Logger log = LoggerFactory.getLogger(RedisRpcGet.class);

    private R result;
    private long readTimeOut;
    private RpcTopicMessage rtm;

    @Override
    public R call() throws RedisRpcException {
        RedisTemplate<String,String> redisTemplate=RedisTemplateUtil.getRedisTemplate();
        long curReadTimeOut=System.currentTimeMillis();
        String serviceRpcKey= RpcContants.getVId(rtm.getUuid());

        log.info("RedisRpcGet pulling remote result from the redisRpc...." +
                "\nresult key【{}】", serviceRpcKey);


        boolean isqueue=true;
        result=null;
        //先从缓存里获取一次，如果存在就直接返回值，如果不存在，则发送topic
        if(rtm.isUserCache()){
            result= JSON.parseObject(redisTemplate.opsForValue().get(serviceRpcKey), R.class);
            if(result==null){
                if(isqueue){
                    redisTemplate.opsForList().leftPush(rtm.getServiceName(),JSON.toJSONString(rtm));
                }else{
                    redisTemplate.convertAndSend(rtm.getServiceName(),rtm);
                }

            }
        }
        //异步获取相关信息
        while(result==null){
            result= JSON.parseObject(redisTemplate.opsForValue().get(serviceRpcKey), R.class);
            if(System.currentTimeMillis()-curReadTimeOut>readTimeOut){
                throw new RedisRpcException("Read the redisRpc is timeout.There may be no registration service.");
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        log.info("RedisRpcGet pulled remote result from the redisRpc...." +
                "\nresult key【{}】" +
                "\nresult content【{}】",serviceRpcKey, JSON.toJSONString(result));
        return result;
    }


    public RedisRpcGet(RpcTopicMessage rtm,long readTimeOut){
        super();
        this.rtm=rtm;
        this.readTimeOut=readTimeOut;
    }

}
