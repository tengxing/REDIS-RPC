package cn.litteleterry.rpc;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.Proxy;

public class RpcRemoteClientBean<T> implements FactoryBean<T> {
    private Class<?> interfaceType;

    public Class<?> getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(Class<?> interfaceType) {
        this.interfaceType = interfaceType;
    }

    public T getObject() {
        return (T)Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[]{interfaceType}, new RedisRpcClientProxy(interfaceType));
        //return (T)new ProxyFactory(interfaceType).getProxy(interfaceType.getClassLoader());
    }

    @Override
    public Class<?> getObjectType() {
        return this.interfaceType;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
