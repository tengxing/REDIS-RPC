package cn.litteleterry.rpc;

import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloServiceImpl implements HelloService{
    @Override
    public String hi(String name) {
        return "hello, "+name+ Math.random();
    }
}
