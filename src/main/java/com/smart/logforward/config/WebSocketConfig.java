package com.smart.logforward.config;

import com.smart.logforward.factory.PrincipalHandshakeHandler;
import com.smart.logforward.factory.WebSocketDecoratorFactory;
import com.smart.logforward.intercepter.SocketChannelIntecepter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketDecoratorFactory webSocketDecoratorFactory;
    @Autowired
    private PrincipalHandshakeHandler principalHandshakeHandler;

    /**
     * 注册端点，发布或者订阅消息的时候需要连接此端点
     * setAllowedOrigins 非必须，*表示允许其他域进行连接
     * withSockJS  表示开始sockejs支持
     */
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/endpoint-websocket").setAllowedOrigins("*")
                .setHandshakeHandler(principalHandshakeHandler)   // 身份认证handler
                .withSockJS();
    }

    /**
     * 配置消息代理(中介)
     * enableSimpleBroker 服务端推送给客户端的路径前缀
     * setApplicationDestinationPrefixes  客户端发送数据给服务器端的一个前缀
     * setUserDestinationPrefix 点对点前缀
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/topic", "/queue");
            registry.setApplicationDestinationPrefixes("/app");
            //registry.setUserDestinationPrefix("/user");

    }

    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(webSocketDecoratorFactory);
        super.configureWebSocketTransport(registration);
    }


    // 拦截器，暂不使用
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        //registration.interceptors( new SocketChannelIntecepter());
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        //registration.interceptors( new SocketChannelIntecepter());
    }





}


