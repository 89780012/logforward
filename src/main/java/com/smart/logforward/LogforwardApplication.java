package com.smart.logforward;

import com.smart.logforward.netty.TcpServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogforwardApplication {

    public static void main(String[] args) {
        SpringApplication.run(LogforwardApplication.class, args);
        //起socket服务
        TcpServer tcpServer = new TcpServer(26666);
        tcpServer.start();
    }

}
