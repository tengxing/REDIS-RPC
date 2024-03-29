package cn.litteleterry.rpc.lib;


import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.util.ClassUtils;

public abstract class RedisRemotingSupport implements BeanClassLoaderAware {


	private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();


	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	/**
	 * Return the ClassLoader that this accessor operates in,
	 * to be used for deserializing and for generating proxies.
	 */
	protected ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}


	/**
	 * Override the thread context ClassLoader with the environment's bean ClassLoader
	 * if necessary, i.e. if the bean ClassLoader is not equivalent to the thread
	 * context ClassLoader already.
	 * @return the original thread context ClassLoader, or {@code null} if not overridden
	 */
	protected ClassLoader overrideThreadContextClassLoader() {
		return ClassUtils.overrideThreadContextClassLoader(getBeanClassLoader());
	}

	/**
	 * Reset the original thread context ClassLoader if necessary.
	 * @param original the original thread context ClassLoader,
	 * or {@code null} if not overridden (and hence nothing to reset)
	 */
	protected void resetThreadContextClassLoader(ClassLoader original) {
		if (original != null) {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

}

