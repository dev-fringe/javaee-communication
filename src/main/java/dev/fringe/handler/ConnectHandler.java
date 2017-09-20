package dev.fringe.handler;

import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
public class ConnectHandler extends ChannelInboundHandlerAdapter {
	
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Client " + ctx.channel().remoteAddress().toString() + " connected");
	}
	
	public void channelInactive(ChannelHandlerContext ctx) {
		System.out.println("Client " + ctx.channel().remoteAddress().toString() + " disconnected");
	}
}
