package cn.litteleterry.rpc.lib;

import org.springframework.beans.factory.InitializingBean;

/**
 * @author:yy
 */
public abstract class RedisBasedRemoteAccessor extends RedisRemoteAccessor implements InitializingBean {

	@Override
	public void afterPropertiesSet() {
		
	}

}

