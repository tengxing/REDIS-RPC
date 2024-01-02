package cn.litteleterry.rpc;

import java.util.UUID;

public class RpcTopicMessage {
    private String serviceName;
    private String methodName;
    private String requestKey;
    private  Object[] args;

    private String uuid;

    private boolean isUserCache = true;
    private long cacheTime;

    public RpcTopicMessage(String serviceName, String methodName, String requestKey, Object[] args,String uuid) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.requestKey = requestKey;
        this.args = args;
        this.uuid = uuid;
    }

    public String getRequestKey() {
        return requestKey;
    }

    public void setRequestKey(String requestKey) {
        this.requestKey = requestKey;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static String getUUID32(){
        return UUID.randomUUID().toString();
    }

    public boolean isUserCache() {

        return isUserCache;
    }

    public void setUserCache(boolean userCache) {
        isUserCache = userCache;
    }

    public long getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(long cacheTime) {
        this.cacheTime = cacheTime;
    }
}
