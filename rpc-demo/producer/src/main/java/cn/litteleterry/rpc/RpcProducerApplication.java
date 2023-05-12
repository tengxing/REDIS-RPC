package cn.litteleterry.rpc;

import cn.litteleterry.rpc.annotation.EnableDiscoveryRpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Arrays;

@EnableDiscoveryRpcClient
@SpringBootApplication
public class RpcProducerApplication implements CommandLineRunner {
    @Autowired
    HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(RpcProducerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        HelloService helloService = (HelloService)ApplicationContextAwareHolder.getBean("helloService");


        RedisTemplate<String,String> redisTemplate = ApplicationContextAwareHolder.getBean(StringRedisTemplate.class);

        DefaultRedisScript redisScript = new DefaultRedisScript();
        //设置返回类型，这步必须要设置
        redisScript.setResultType(Long.class);
        //设置脚本
        //redisScript.setScriptText("redis.call('get','name')");
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("del_key.lua")));
        //执行
        Object result = redisTemplate.execute(redisScript, Arrays.asList(new String[]{"*view*"}));
        System.out.println(result);
    }

    //@Test
    public void test(){
//        RpcRemoteClientProxyBean serviceBean = new RpcRemoteClientProxyBean();
//        serviceBean.setInterfaceType(HelloService.class);
//
//        HelloService helloService = (HelloService)serviceBean.getObject();
//        helloService.hi();
    }
}

