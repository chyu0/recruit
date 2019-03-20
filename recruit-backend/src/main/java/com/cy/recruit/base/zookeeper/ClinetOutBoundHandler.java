package com.cy.recruit.base.zookeeper;

import com.sun.org.apache.xpath.internal.operations.String;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.FutureListener;

import java.net.SocketAddress;

public class ClinetOutBoundHandler extends ChannelOutboundHandlerAdapter{

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        ctx.connect(remoteAddress, localAddress, promise);
        promise.addListener(new FutureListener<Object>() {
            @Override
            public void operationComplete(Future<Object> objectFuture) throws Exception {
                System.out.println("连接成功clientoutboundhander promise");
                ByteBuf byteBuf = Unpooled.buffer(1024);
                byteBuf.writeBytes("客户端outbound write".getBytes());
                ctx.channel().writeAndFlush(byteBuf);
            }
        });
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.print("write--------->" + msg);
        ByteBuf buf = (ByteBuf)msg;
        if(buf.hasArray()){
            byte[] array = buf.array();

        }
        ctx.write(msg, promise);
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        System.out.println("flush--------->" + ctx);
        ctx.flush();
    }

    @Override
    public void read(ChannelHandlerContext ctx) throws Exception {
        System.out.println("read--------->" + ctx);
        ctx.flush();
    }

    @Override
    public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        System.out.println("断开连接");
        ctx.disconnect(promise);
    }
}
