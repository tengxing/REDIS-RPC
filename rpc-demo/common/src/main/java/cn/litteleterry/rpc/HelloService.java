package cn.litteleterry.rpc;

import cn.litteleterry.rpc.annotation.RRemoteClient;

@RRemoteClient("helloService")
public interface HelloService {
    String hi(String name);

    String hello(String name,String age);
}
