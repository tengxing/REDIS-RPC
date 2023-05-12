package cn.litteleterry.rpc;

import cn.litteleterry.rpc.annotation.EnableRpcClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableRpcClients
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
        while (true){
            System.out.println("======>"+helloService.hi("args"));
            Thread.sleep(3000);
            System.out.println("======>"+helloService.hello("terry","25"));
            Thread.sleep(3000);
        }
    }

    @RequestMapping("/hello")
    public String hello(String args)  {
        return helloService.hi(args);
    }
}
