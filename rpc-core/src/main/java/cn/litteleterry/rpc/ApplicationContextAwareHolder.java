package cn.litteleterry.rpc;

import cn.litteleterry.rpc.event.RedisRpcTopicRegisterEvent;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

@Component
public class ApplicationContextAwareHolder implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static Map<String, Object> getAllServices() {
        return applicationContext.getBeansWithAnnotation(
                Service.class);
    }

    public static void publishEvent(RedisRpcTopicRegisterEvent event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> cls){
        return applicationContext.getBean(cls);
    }

    public static Object  getBean(String name){
        return applicationContext.getBean(name);
    }

    public static void registerBeanDefinition(BeanDefinitionHolder beanDefinitionHolder) {
        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableContext.getBeanFactory();
        BeanDefinitionReaderUtils.registerBeanDefinition(beanDefinitionHolder, beanDefinitionRegistry);
    }

    public static void registerBean(String beanId, String className, Map propertyMap) {
        ConfigurableApplicationContext configurableContext = (ConfigurableApplicationContext) applicationContext;
        BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) configurableContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder =BeanDefinitionBuilder.genericBeanDefinition(className);
        if(propertyMap!=null){
            Iterator<?> entries = propertyMap.entrySet().iterator();
            Map.Entry<?, ?> entry;
            while (entries.hasNext()) {
                entry = (Map.Entry<?, ?>) entries.next();
                String key = (String) entry.getKey();
                Object val = entry.getValue();
                beanDefinitionBuilder.addPropertyValue(key, val);
            }

            BeanDefinition beanDefinition=beanDefinitionBuilder.getBeanDefinition();
            beanDefinitionRegistry.registerBeanDefinition(beanId,beanDefinition);
        }
    }

//    public static RedisTemplate<String,Object> getRpcRedisTemplate(){
//        if(redisTemplate!=null){
//            return redisTemplate;
//        }
//        return (RedisTemplate<String, Object>) getSpringBean("redisRpcMqTemplate");
//    }
}
