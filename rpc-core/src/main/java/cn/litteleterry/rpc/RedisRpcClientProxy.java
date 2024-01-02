package cn.litteleterry.rpc;

import cn.litteleterry.rpc.annotation.RRemoteClient;
import cn.litteleterry.rpc.util.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RedisRpcClientProxy implements InvocationHandler, Serializable {
	private static final Logger log = LoggerFactory.getLogger(RedisRpcClientProxy.class);

	protected RedisRpcProxyFactory _factory;

	protected RpcRemoteClientBean clientBean;

	private Class<?> _interfaceType;

	protected RedisRpcClientProxy(Class<?> interfaceType){
		_interfaceType = interfaceType;
	}


	@Override
	public Object invoke(Object o, Method method, Object[] objects) {
		RRemoteClient anno = this._interfaceType.getAnnotationsByType(RRemoteClient.class)[0];
		String methodName = method.getName();
		RpcTopicMessage topicMessage = new RpcTopicMessage(anno.value(),methodName,null,objects, RpcTopicMessage.getUUID32());
		CompletableFuture<Object> completableFuture = new CompletableFuture();
		fetchRemoteRpcResult(completableFuture,topicMessage);
		try {
			return completableFuture.get(3, TimeUnit.SECONDS);
		} catch (TimeoutException e) {
			log.error("RedisRpcGet pulled remote result from the redisRpc timeout ....{}",topicMessage.getUuid());
		} catch (Exception e) {
			log.error("RedisRpcGet pulled remote result from the redisRpc exception ....{}",e.getMessage(),e);
		}
		return null;
	}

	public void fetchRemoteRpcResult(CompletableFuture<Object> completableFuture,RpcTopicMessage topicMessage){
		CompletableFuture.supplyAsync(() -> {
			R result = null;
			try {
				result = ThreadPool.getServiceBack(topicMessage, 100000);
			}catch (Exception e){
				e.printStackTrace();
			}
			//handler 降级
//            if (true !=r.getSuccess()){
//                System.out.println("远程调用失败，等待降级处理， requestKey:"+requestKey+"\t====> "+result);
//            }
			//System.out.println("远程调用返回， requestKey:"+requestKey+"\t====> "+result);
			completableFuture.complete(result.getData());
			return result;
		});
	}



//	@Override
//	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//		String topicName=_type.getSimpleName();
//		String rpcServiceName=_factory.getRpcServiceName();
//		long readTimeOut=_factory.getReadTimeout();
//		if(readTimeOut<=0l){
//			readTimeOut= redisRpc.boot.RpcContants.REDIS_READTIMEOUT;
//		}
//		if(rpcServiceName!=null){
//			topicName=rpcServiceName;
//		}else{
//			topicName=toLowerCaseFirstOne(topicName);
//		}
//		long cacheTime=_factory.getCacheTime();
//		if(cacheTime<=0l){
//			cacheTime= RpcContants.REDISRPC_CACHEDTIME;
//		}
//
//		boolean isuserCache=_factory.isUseCache();
//
//		RedisTemplate<String,Object> redisTemplate= RedisRPCSpringApplicationContextHolder.getRpcRedisTemplate();
//		RedisTopicMessage rtm=new RedisTopicMessage(method.getName(),args,topicName,isuserCache,cacheTime);
//		//先开始获取数据
//		Result result=null;
//		try{
//			//多线程获取返回值
//			result=ThreadPool.getServiceBack(rtm,readTimeOut);
//			if(!result.isSuccess()){
//				throw new RedisRpcException(result.getMessage());
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return result.getData();
//	}
//

	public static String toLowerCaseFirstOne(String s){
	  if(Character.isLowerCase(s.charAt(0)))
	    return s;
	  else
	    return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
}
