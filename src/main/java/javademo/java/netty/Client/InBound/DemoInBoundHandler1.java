package javademo.java.netty.Client.InBound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class DemoInBoundHandler1 extends ChannelInboundHandlerAdapter{
	
	
	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		System.out.println("=======channelRegistered=====");
	}
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelActive(ctx);
		System.out.println("=======channelActive=====");

	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf bytes = (ByteBuf) msg;
		CharSequence chs=   bytes.getCharSequence(0,bytes.readableBytes(),CharsetUtil.UTF_8);
		System.out.println( chs );
		ctx.writeAndFlush(bytes);
	}

}
