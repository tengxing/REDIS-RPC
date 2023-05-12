package cn.litteleterry.rpc.annotation;

import cn.litteleterry.rpc.RpcServiceSubConsumer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({RpcServiceSubConsumer.class})
public @interface EnableDiscoveryRpcClient {
    boolean autoRegister() default true;
}
