package dev.fringe.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
@Sharable
public class ApplicationHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	private String sql;

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String stringMessage = (String) msg;
		System.out.println(stringMessage + ", bean's sql query :" + sql);
	}
}
