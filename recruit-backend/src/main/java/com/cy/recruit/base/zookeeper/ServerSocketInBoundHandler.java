package com.cy.recruit.base.zookeeper;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ServerSocketInBoundHandler extends ChannelInboundHandlerAdapter{

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        System.out.print("ServerSocketInBoundHandler read:");
        while(buf.readableBytes() > 0){
            System.out.print(buf.readBytes(buf.readableBytes()).toString(CharsetUtil.UTF_8));
        }
        System.out.println();

        if(buf.isWritable()){
            buf.writeBytes(new String("服务端给出响应").getBytes());
        }
        ctx.channel().writeAndFlush(buf);
        ctx.fireChannelActive();
    }

}
