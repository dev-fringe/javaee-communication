package dev.fringe.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import dev.fringe.channel.ApplicationChannelRepository;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Component
@Qualifier("serverHandler")
@Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	private ApplicationChannelRepository channelRepository;

	@Autowired
	private String sql;

	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelActive();
		String channelKey = ctx.channel().remoteAddress().toString();
		channelRepository.put(channelKey, ctx.channel());
		ctx.writeAndFlush("Your channel key is " + channelKey + "\n\r");
	}

	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String stringMessage = (String) msg;
		System.out.println(stringMessage + ", bean's sql query :" + sql);
	}

	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
	}

	public void channelInactive(ChannelHandlerContext ctx) {
		String channelKey = ctx.channel().remoteAddress().toString();
		this.channelRepository.remove(channelKey);

	}

}
