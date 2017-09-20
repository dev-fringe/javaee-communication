package dev.fringe.socket;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import dev.fringe.channel.ApplicationChannelInitializer;
import dev.fringe.channel.ApplicationChannelRepository;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Component
@Configuration
public class SocketServer {

	private int tcpPort = 9090;

	private int bossCount = 10;

	private int workerCount = 10;

	private boolean keepAlive = true;

	private int backlog = 1000;

	@Autowired
	private ApplicationChannelInitializer applicationChannelInitializer;

	@Autowired
	private ServerBootstrap serverBootstrap;

	@Autowired
	private InetSocketAddress tcpSocketAddress;

	private Channel serverChannel;

	@SuppressWarnings("unchecked")
	@Bean(name = "serverBootstrap")
	public ServerBootstrap bootstrap() {
		ServerBootstrap b = new ServerBootstrap();
		b.group(new NioEventLoopGroup(bossCount), new NioEventLoopGroup(workerCount))
				.channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.DEBUG))
				.childHandler(applicationChannelInitializer);
		Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
		Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
		for (@SuppressWarnings("rawtypes")
		ChannelOption option : keySet) {
			b.option(option, tcpChannelOptions.get(option));
		}
		return b;
	}

	public Map<ChannelOption<?>, Object> tcpChannelOptions() {
		Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
		options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
		options.put(ChannelOption.SO_BACKLOG, backlog);
		return options;
	}

	@Bean
	public InetSocketAddress tcpSocketAddress() {
		return new InetSocketAddress(tcpPort);
	}

	@Bean
	public ApplicationChannelRepository channelRepository() {
		return new ApplicationChannelRepository();
	}

	public void start() throws Exception {
		serverChannel = serverBootstrap.bind(tcpSocketAddress).sync().channel().closeFuture().sync().channel();
	}

	@PreDestroy
	public void stop() throws Exception {
		serverChannel.close();
		serverChannel.parent().close();
	}

	public ServerBootstrap getServerBootstrap() {
		return serverBootstrap;
	}

	public void setServerBootstrap(ServerBootstrap serverBootstrap) {
		this.serverBootstrap = serverBootstrap;
	}

	public InetSocketAddress getTcpSocketAddress() {
		return tcpSocketAddress;
	}

	public void setTcpSocketAddress(InetSocketAddress tcpSocketAddress) {
		this.tcpSocketAddress = tcpSocketAddress;
	}
}
