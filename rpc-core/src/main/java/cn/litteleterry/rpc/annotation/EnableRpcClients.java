package cn.litteleterry.rpc.annotation;

import cn.litteleterry.rpc.RRemoteClientRegistery;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RRemoteClientRegistery.class)
public @interface EnableRpcClients {
}
