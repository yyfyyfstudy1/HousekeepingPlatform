package com.usyd.capstone;

import com.usyd.capstone.common.component.NotificationServer;
import com.usyd.capstone.common.component.WebSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CapstoneApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext configurableApplicationContext =  SpringApplication.run(CapstoneApplication.class, args);
        //解决WebSocket不能注入的问题
        WebSocketServer.setApplicationContext(configurableApplicationContext);
        NotificationServer.setApplicationContext(configurableApplicationContext);
    }

}
