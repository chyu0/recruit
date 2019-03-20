package com.cy.recruit.base.zookeeper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ClientSocketInBoundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("链接" + ctx.channel().remoteAddress());
        ByteBuf buf = Unpooled.buffer(1024);
        if(buf.isWritable()){
            buf.writeBytes(new String("客户端接受服务端连接成功").getBytes());
        }
        ctx.channel().writeAndFlush(buf);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("失活");
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf)msg;
        System.out.print("ClientSocketInBoundHandler read:");
        while(buf.readableBytes() > 0){
            System.out.print(buf.readBytes(buf.readableBytes()).toString(CharsetUtil.UTF_8));
        }
        System.out.println();
        ctx.fireChannelRead(msg);
    }
}
