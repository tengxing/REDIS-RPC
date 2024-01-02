package cn.litteleterry.rpc;

public interface RpcConsumerHandler {
    void handlerRecord(RpcTopicMessage topicMessage);
}
