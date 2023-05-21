package javademo.java.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

public class SelectThreadGroup {
	
	SelectorThread[] sts;
	ServerSocketChannel server; 
	AtomicInteger ticket = new AtomicInteger(1);
	SelectThreadGroup workerGroup = null;
	
	public void setWorkers(SelectThreadGroup worker) {
		workerGroup = worker;
	}
	

	public SelectThreadGroup(int i) throws IOException   {
		sts = new SelectorThread[i];
		for (int j = 0; j < sts.length; j++) {
			sts[j] = new SelectorThread(this);
			new Thread(sts[j]).start();
		}
	}

	
	public void bind(int port) throws IOException {

		server = ServerSocketChannel.open();
		server.configureBlocking(false);
		server.bind(new InetSocketAddress(port));
		nextSelector(server);
	}
	
	public void nextSelector(Channel c) {
		
		//register方法会向阻塞的selector.select(),仍然阻塞
		SelectorThread st = sts[ticket.incrementAndGet()%sts.length];
//       ServerSocketChannel s = (ServerSocketChannel) c;
//		try {
//			s.register(st.selector, SelectionKey.OP_ACCEPT);
//			st.selector.wakeup();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		if(c instanceof ServerSocketChannel) {
			st.queue.add(c);
			st.selector.wakeup();
			st.stgroup = workerGroup;
		}else if (c instanceof SocketChannel) {
			st.queue.add(c);
			st.selector.wakeup();
		}
		 
	}
}
