package dev.fringe.socket;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import dev.fringe.channel.SocketChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

@Component
public class SocketServer {

	@Value("${server.port:9090}")
	private int port;

	@Autowired
	private SocketChannelInitializer applicationChannelInitializer;

	@PostConstruct
	public void start() throws Exception {
		ServerBootstrap b = new ServerBootstrap();
		NioEventLoopGroup group = new NioEventLoopGroup();
		b.group(new NioEventLoopGroup(), new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
				.localAddress(port).childHandler(applicationChannelInitializer);
		try {
			b.bind().sync().channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
