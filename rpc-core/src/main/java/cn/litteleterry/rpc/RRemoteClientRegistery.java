package cn.litteleterry.rpc;

import cn.litteleterry.rpc.annotation.RRemoteClient;
import cn.litteleterry.rpc.vo.RemoteClientInfo;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RRemoteClientRegistery implements ImportBeanDefinitionRegistrar {
    private static final Logger log = LoggerFactory.getLogger(RRemoteClientRegistery.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        this.registerRedisClients(importingClassMetadata,registry);
    }

    private void registerRedisClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Set<Class<?>> typesAnnotatedWith = this.getRedisRPCClientCls();
        for (Class<?> cls:typesAnnotatedWith){
            registerBean(cls, registry);
            registerTopic(cls);
        }
    }

    private void registerTopic(Class<?> cls) {
        if (!cls.isAnnotationPresent(RRemoteClient.class)){
            return;
        }

        RemoteClientInfo remoteClientInfo = new RemoteClientInfo();
        RRemoteClient anno = cls.getAnnotation(RRemoteClient.class);
        String serviceName = cls.getSimpleName();
        remoteClientInfo.setValue(serviceName);
        remoteClientInfo.setVersion("v1");
        remoteClientInfo.setTimeout(5);
        CompletableFuture.runAsync(()-> RedisTemplateUtil.getRedisTemplate().opsForHash().put("RRPC", serviceName, remoteClientInfo));
       // ApplicationContextAwareHolder.publishEvent(new RedisRpcTopicRegisterEvent(patternTopic, patternTopic));
    }

    private Set<Class<?>> getRedisRPCClientCls() {
        //方式①：获取全部service,并基于@RRemoteClient注解过滤（重叠租借@Service）
//        Map<String, Object> services = ApplicationContextAwareHolder.getAllServices();
//        List<Class<?>> clientCls = services.values().stream()
//                .map(Object::getClass)
//                .filter(aClass -> aClass.isAnnotationPresent(RRemoteClient.class)).collect(Collectors.toList());
//        return new HashSet<>(clientCls);

        //方式②：获取包下@RRemoteClient注解
        Reflections reflections = new Reflections("cn.litteleterry.rpc");
        return reflections.getTypesAnnotatedWith(RRemoteClient.class);
    }

    private void registerBean(Class<?> cls, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cls);
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition)beanDefinitionBuilder.getRawBeanDefinition();

        beanDefinition.getPropertyValues().add("interfaceType", cls);
        beanDefinition.setBeanClass(RpcRemoteClientBean.class);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
//        FeignClientFactoryBean factoryBean = new FeignClientFactoryBean();
//        factoryBean.setBeanFactory(beanFactory);
//        factoryBean.setName(name);
//        factoryBean.setContextId(contextId);
//        factoryBean.setType(clazz)

        String className = cls.getName();
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition,className,null);
        if (Objects.isNull(registry)){
            ApplicationContextAwareHolder.registerBeanDefinition(beanDefinitionHolder);;
        }else {
            BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);
        }
    }
}
