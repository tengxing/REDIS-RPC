package cn.litteleterry.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.PrimitiveIterator;

@Import(RRemoteClientRegistery.class)
@SpringBootApplication
@RestController
public class RpcConsumerApplication implements CommandLineRunner {

    @Autowired
    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(RpcConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("======>"+helloService.hi("args"));
    }

    @RequestMapping("/hello")
    public String hello(String args)  {
        return helloService.hi(args);
    }
}
