package cn.litteleterry.rpc.event.listener;

import cn.litteleterry.rpc.event.RedisRpcTopicRegisterEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.stereotype.Component;

@Component
public class RedisRpcTopicRegisterListener implements ApplicationListener<RedisRpcTopicRegisterEvent> {
    private static final Logger log = LoggerFactory.getLogger(RedisRpcTopicRegisterListener.class);

    @Override
    public void onApplicationEvent(RedisRpcTopicRegisterEvent event) {
        PatternTopic patternTopic = (PatternTopic)event.getSource();
        log.info("register redis topic watch cn.litteleterry.rpc.event.listener.RedisRpcTopicRegisterListener onApplicationEvent {}",patternTopic);
        //todo
        // 先注册服务
//				for (Topic topic : topicSet) {
//					String topicName = topic.getTopic();
//					//redisTemplate.opsForHash().put(RpcContants.REDISRPC_POOL, topicName, topicName);
//					System.out.println("RedisRpcService :'"+topicName+"' is  registed." );
//				}

        //				// 定义一个消费者监听器
//				Receiver receiver = (Receiver) ApplicationContextAwareHolder.getBean("receiver");
//				Map map = new HashMap();
//				JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
//				map.put("serializer", serializer);
//				map.put("delegate", receiver);
//				map.put("defaultListenerMethod", "receiveMessage");
//				RedisRPCSpringApplicationContextHolder.registerBean("listenerAdapter", MessageListenerAdapter.class.getName(), map);
//
//				MessageListenerAdapter listenerAdapter = (MessageListenerAdapter) RedisRPCSpringApplicationContextHolder
//						.getSpringBean("listenerAdapter");
//				Map map2 = new HashMap();
//				map2.put("connectionFactory", redisTemplate.getConnectionFactory());
//				RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//				container.setConnectionFactory(redisTemplate.getConnectionFactory());
//
//				Map<MessageListener, Set<Topic>> listenerTopics = new ConcurrentHashMap<MessageListener, Set<Topic>>();
//
//				listenerTopics.put(listenerAdapter, topicSet);
//
//				map2.put("messageListeners", listenerTopics);
//				ApplicationContextAwareHolder.registerBean("container", RedisMessageListenerContainer.class.getName(),
//						map2);
//				// 再加入消费者
//				RedisMessageListenerContainer c = (RedisMessageListenerContainer) ApplicationContextAwareHolder
//						.getBean("container");
//				c.start();
//
    }
}
