package javademo.java.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;

public class ByteBufDemo {
	
	public static void main(String[] args) {
		ByteBufAllocator.DEFAULT.buffer(100, 200);
		UnpooledByteBufAllocator.DEFAULT.directBuffer(100, 200);
		UnpooledByteBufAllocator.DEFAULT.heapBuffer(100, 200);
	}

}
