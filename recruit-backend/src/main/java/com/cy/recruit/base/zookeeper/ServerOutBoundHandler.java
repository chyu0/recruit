package com.cy.recruit.base.zookeeper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.SocketAddress;

public class ServerOutBoundHandler extends ChannelOutboundHandlerAdapter{

    @Override
    public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        System.out.println("服务端绑定到本地地址成功" + localAddress);

        ctx.bind(localAddress, promise);

        promise.addListener(new FutureListener() {
            @Override
            public void operationComplete(Future future) throws Exception {
                System.out.println("服务端绑定到本地地址成功" + localAddress);
            }
        });
    }


    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("客户端断开连接");
        ctx.disconnect(promise);
    }
}
