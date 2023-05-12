package cn.litteleterry.rpc;

import org.reflections.Reflections;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Set;

public class RRemoteClientRegistery implements ImportBeanDefinitionRegistrar {
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        this.registerRedisClients(importingClassMetadata,registry);
    }

    private void registerRedisClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        Reflections reflections = new Reflections("cn.litteleterry.rpc");
        //获取带DataClass注解的类
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(RRemoteClient.class);
        for (Class c:typesAnnotatedWith){
            //通过循环打印出权限定类名
            System.out.println(c.getName());
            registerBean(c,registry);
        }

//        FeignClientFactoryBean factoryBean = new FeignClientFactoryBean();
//        factoryBean.setBeanFactory(beanFactory);
//        factoryBean.setName(name);
//        factoryBean.setContextId(contextId);
//        factoryBean.setType(clazz)
//        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className, qualifiers);
//        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private void registerBean(Class cls, BeanDefinitionRegistry registry) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(cls);
        GenericBeanDefinition beanDefinition = (GenericBeanDefinition)beanDefinitionBuilder.getRawBeanDefinition();

        beanDefinition.getPropertyValues().add("interfaceType", cls);
        beanDefinition.setBeanClass(RpcRemoteClientProxyBean.class);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);

        String className = cls.getName();
        BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition,className,null);
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, registry);

    }
}
