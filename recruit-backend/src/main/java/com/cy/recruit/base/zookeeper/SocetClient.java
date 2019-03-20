package com.cy.recruit.base.zookeeper;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SocetClient {

    public static void connect(){
        EventLoopGroup group = new NioEventLoopGroup();

        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).
                    remoteAddress(new InetSocketAddress("127.0.0.1",  8000)).
                    handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel socketChannel) throws Exception {

                                    ChannelPipeline line = socketChannel.pipeline();
                                    line.addLast(new ClientSocketInBoundHandler());
                                    line.addLast(new ClinetOutBoundHandler());
                                }
                            }
                    );
            final ChannelFuture f = bootstrap.connect();
            f.channel().closeFuture().sync();
        }catch (Exception e){

        }
    }

    public static void main(String[] args){
        try{
            connect();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
