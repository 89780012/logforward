package com.smart.logforward.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import com.smart.logforward.netty.ServerIdleStateTrigger;
/**
 * <p>用于初始化服务器端涉及到的所有<code>Handler</code></p>
 */
public class ServerHandlerInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel ch) throws Exception {
        // 在5秒内没有接收到数据， 断开连接
        ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(5, 0, 0));
        // 在规定时间内未收到客户端的任何数据包, 将主动断开该连接
        //ch.pipeline().addLast("idleStateTrigger", new ServerIdleStateTrigger());
        // 粘包问题
        ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
        ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(4));
        ch.pipeline().addLast("decoder", new StringDecoder());
        ch.pipeline().addLast("encoder", new StringEncoder());
        ch.pipeline().addLast("bizHandler", new ServerBizHandler());
    }

}