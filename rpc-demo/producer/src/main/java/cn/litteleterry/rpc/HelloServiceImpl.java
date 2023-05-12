package cn.litteleterry.rpc;

import org.springframework.stereotype.Service;

@Service("helloService")
public class HelloServiceImpl implements HelloService{
    @Override
    public String hi(String name) {
        return "hello, "+name+ Math.random();
    }

    @Override
    public String hello(String name, String age) {
        return "hello, "+name +",your age is"+age+"\t"+ Math.random();
    }
}
