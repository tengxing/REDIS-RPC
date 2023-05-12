package cn.litteleterry.rpc;

import org.springframework.web.bind.annotation.GetMapping;

public class BaseC {

    @GetMapping("/a")
    public Object getA(){
        return "";
    }
}
