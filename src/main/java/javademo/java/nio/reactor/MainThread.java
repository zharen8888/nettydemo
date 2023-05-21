package javademo.java.nio.reactor;

import java.io.IOException;

public class MainThread {

	
	public static void main(String[] args) throws IOException {
		SelectThreadGroup boss = new SelectThreadGroup(1);
		SelectThreadGroup worker = new SelectThreadGroup(3);
		boss.setWorkers(worker);
		boss.bind(8800);
		//boss 线程数多的话，可以监听多个端口
		//boss.bind(8800);
		//boss.bind(8800);
	}
}
