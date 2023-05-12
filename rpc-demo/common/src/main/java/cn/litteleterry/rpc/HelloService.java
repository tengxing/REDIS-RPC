package cn.litteleterry.rpc;

import org.springframework.stereotype.Service;

@RRemoteClient("helloService")
public interface HelloService {
    String hi(String name);
}
