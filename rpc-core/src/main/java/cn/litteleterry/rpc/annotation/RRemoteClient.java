package cn.litteleterry.rpc.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RRemoteClient {
    String value();

    String alias() default "";

    String version() default "";

    String beanId() default "";

    String proto() default "application/octet-stream";

    int timeout() default -1;
}

