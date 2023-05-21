package javademo.java.nio.reactor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

public class SelectorThread extends ThreadLocal<LinkedBlockingDeque<Channel>> implements Runnable{
	
	Selector selector = null;
	LinkedBlockingDeque<Channel> queue = get();
	SelectThreadGroup stgroup = null;
	public SelectorThread(SelectThreadGroup selectThreadGroup) throws IOException {
		selector = Selector.open();
		stgroup = selectThreadGroup;
	}

	@Override
	protected LinkedBlockingDeque<Channel> initialValue() {
		return new LinkedBlockingDeque<Channel>();
	}

	public void run() {
		while(true) {
			try {
				int nums = selector.select();//阻塞，除非wakeup()
				if(nums>0) {
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> iter = keys.iterator();
					while(iter.hasNext()) {
						 SelectionKey key = iter.next();
						 iter.remove();
						 if(key.isAcceptable()) {
							 acceptHandle(key);
						 }else if (key.isReadable()) {
							 readHandle(key);
						 }
					}
				}
				if(!queue.isEmpty()) {
					try {
						Channel c = queue.take();
						if(c instanceof ServerSocketChannel) {
							ServerSocketChannel server = (ServerSocketChannel) c;
							server.register(selector, SelectionKey.OP_ACCEPT);
						}else if (c instanceof SocketChannel) {
							SocketChannel client = (SocketChannel) c;
							ByteBuffer buffer = ByteBuffer.allocateDirect(4096);
							client.register(selector,SelectionKey.OP_READ,buffer);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void acceptHandle(SelectionKey key) {
		ServerSocketChannel server = (ServerSocketChannel) key.channel();
		try {
			SocketChannel client = server.accept();
			client.configureBlocking(false);
			System.out.println("accept ok::"+client);
			stgroup.nextSelector(client);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	public void readHandle(SelectionKey key) {
		SocketChannel client = (SocketChannel) key.channel();
		ByteBuffer buffer = (ByteBuffer) key.attachment();
		buffer.clear();
		while(true) {
			try {
				int num = client.read(buffer);
				if(num >0) {
					buffer.flip(); 
					while(buffer.hasRemaining()) {
						System.out.println("read this");
						client.write(buffer);		
					}
					buffer.clear();
				}else if(num == 0) {
					break;
				}else {
					//客户端断开
					key.channel();
					break;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
