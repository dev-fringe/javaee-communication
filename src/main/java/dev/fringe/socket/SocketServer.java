package dev.fringe.socket;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.fringe.channel.SocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SocketServer {
	
	@Value("${server.port:9090}")
	private int port;
	
	@Autowired
	private SocketChannelInitializer applicationChannelInitializer;

	private Channel serverChannel;

	public ServerBootstrap serverBootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(new NioEventLoopGroup(10), new NioEventLoopGroup(10))
				.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(applicationChannelInitializer);
		Map<ChannelOption<?>, Object> tcpChannelOptions = new HashMap<ChannelOption<?>, Object>();
		tcpChannelOptions.put(ChannelOption.SO_KEEPALIVE, true);
		tcpChannelOptions.put(ChannelOption.SO_BACKLOG, 1000);
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for (ChannelOption option : keySet) {
			b.option(option, tcpChannelOptions.get(option));
		}
		return b;
	}

	@PostConstruct
	public void start() throws Exception {
		serverChannel = serverBootstrap().bind(new InetSocketAddress(port)).sync().channel().closeFuture().sync().channel();
	}

	@PreDestroy
	public void stop() throws Exception {
		serverChannel.close();
		serverChannel.parent().close();
	}
}
