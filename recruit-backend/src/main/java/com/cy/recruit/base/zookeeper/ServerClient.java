package com.cy.recruit.base.zookeeper;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

public class ServerClient {

    public static void bind(int port) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup).
                    channel(NioServerSocketChannel.class).//指定channel使用Nio传输
                    localAddress(new InetSocketAddress(port)).//执行端口设置套接字地址
                    childHandler(new ChannelInitializer<SocketChannel>() {//添加echoServerHandler到Channel的channelpipeline上
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline channelPipeline = socketChannel.pipeline();
                    channelPipeline.addLast(new ServerSocketInBoundHandler());
                    channelPipeline.addLast(new ServerOutBoundHandler());
                }
            });
            ChannelFuture f = serverBootstrap.bind().sync();//异步绑定服务器，调用sync()方法阻塞等待直到绑定完成
            f.channel().closeFuture().sync();//获得Channel的closefutrue，并且阻塞当前线程直到它完成
        } catch (InterruptedException e) {
            eventLoopGroup.shutdownGracefully().sync();
        }
    }


    public static void main(String[] args){
        try{
            bind(8000);
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
