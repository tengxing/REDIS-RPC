package cn.litteleterry.rpc;

import java.util.Objects;

public class ServiceTopicMessage {
    private String serviceName;
    private String methodName;
    private String requestKey;
    private  Object[] args;

    public ServiceTopicMessage(String serviceName, String methodName,String requestKey,Object[] args) {
        this.serviceName = serviceName;
        this.methodName = methodName;
        this.requestKey = requestKey;
        this.args = args;
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
}
