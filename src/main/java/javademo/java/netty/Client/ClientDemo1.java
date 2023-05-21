package javademo.java.netty.Client;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javademo.java.netty.Client.InBound.DemoInBoundHandler1;

public class ClientDemo1 {
	
	public static void main(String[] args) throws InterruptedException {
		NioEventLoopGroup threadGroup = new NioEventLoopGroup(1);
		NioSocketChannel channel = new NioSocketChannel();
		
		channel.pipeline().addLast(new DemoInBoundHandler1());
		threadGroup.register(channel);
		
		
		ChannelFuture connect = channel.connect(new InetSocketAddress(8888));
		connect.sync();
		ByteBuf msg = Unpooled.copiedBuffer("aaaa".getBytes());
		ChannelFuture write = channel.writeAndFlush(msg);
		write.sync();
		
		connect.channel().closeFuture().sync();
		
		
		
	}

}
