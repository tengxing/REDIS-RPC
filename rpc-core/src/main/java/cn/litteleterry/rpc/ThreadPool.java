package cn.litteleterry.rpc;

import cn.litteleterry.rpc.util.PropertiesUtils;
import cn.litteleterry.rpc.util.R;

import java.util.concurrent.*;


public class ThreadPool {
	
	private static int corePoolSize;
	private static int produceTaskMaxNumber;
	private static int produceTaskSleepTime;
	private static int workQueue;
	
	private static ThreadPoolExecutor threadPool=null;
	static{
		/**
		 * corePoolSize 	核心线程池大小
			maximumPoolSize 	最大线程池大小
			keepAliveTime 	线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
			TimeUnit 	keepAliveTime时间单位
			workQueue 	阻塞任务队列
			threadFactory 	新建线程工厂
			RejectedExecutionHandler 	当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理
		 */
		corePoolSize=getValueByPro("redisRpc.ThreadPool.corePoolSize",5);
		produceTaskMaxNumber=getValueByPro("redisRpc.ThreadPool.maximumPoolSize",20);
		produceTaskSleepTime=getValueByPro("redisRpc.ThreadPool.produceTaskSleepTime",3);
		workQueue=getValueByPro("redisRpc.ThreadPool.workQueue",10);
		
		System.out.println(corePoolSize+"-"+produceTaskMaxNumber+"-"+produceTaskSleepTime+"-"+workQueue);

		threadPool = new ThreadPoolExecutor(corePoolSize, produceTaskMaxNumber, produceTaskSleepTime,
				TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(workQueue),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	
	@SuppressWarnings("unchecked")
	public static R getServiceBack(RpcTopicMessage rmt, long readTimeOut) throws RedisRpcException, InterruptedException, ExecutionException{
		cn.litteleterry.rpc.RedisRpcGet t=new cn.litteleterry.rpc.RedisRpcGet(rmt,readTimeOut);
		Future k = threadPool.submit(t);
		R result = null;
		result=(R)k.get();
		return result;
	}
	
	public static Integer strToInt(String str,int defaultValue){
		if(str.matches("^\\d+$")){
			return Integer.parseInt(str);
		}else{
			return defaultValue;
		}
	}
	
	public static Integer getValueByPro(String key,int defaultValue){
		try{
			if(PropertiesUtils.getPropertiesValue(key)!=null){
				String v=PropertiesUtils.getPropertiesValue("${"+key+"}").trim();
				return strToInt(v,defaultValue);
			}else{
				return defaultValue;
			}
		}catch(Exception e){
			return defaultValue;
		}
	}
}