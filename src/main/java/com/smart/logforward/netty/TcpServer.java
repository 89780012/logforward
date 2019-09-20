package com.smart.logforward.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TcpServer {
    private int port;
    private ServerHandlerInitializer serverHandlerInitializer;

    public TcpServer(int port) {
        this.port = port;
        this.serverHandlerInitializer = new ServerHandlerInitializer();
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(this.serverHandlerInitializer);
            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(port).sync();

            System.out.println("Server start listen at " + port);

            // 开启了一个channel 的监听器，负责监听channel是否关闭的状态
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 26666;
        new TcpServer(port).start();
    }
}