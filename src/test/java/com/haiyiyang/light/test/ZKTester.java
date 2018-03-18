package com.haiyiyang.light.test;

import org.apache.zookeeper.KeeperException;

import com.haiyiyang.light.meta.props.AppProps;
import com.haiyiyang.light.subscription.LightSubscription;

public class ZKTester {

	private static final String REGISTRY_LOCAL = "127.0.0.1:2181";

	public static void main(String[] args) {
		new Thread(new ThreadA("Thread-A")).start();

		new Thread(new ThreadB("Thread-B")).start();

		new Thread(new ThreadC("Thread-C")).start();

//		try {
//			Thread.sleep(100000000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
	}

	static class ThreadA implements Runnable {

		private String name;

		public ThreadA(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println("Thread: " + name + " \t");
			LightSubscription subscription = LightSubscription.getSubscription(new AppProps(REGISTRY_LOCAL));
//			try {
//				subscription.createLightPath();
//				subscription.createPath("/test");
//				subscription.createPath("/test/11111");
//			} catch (KeeperException | InterruptedException e1) {
//				e1.printStackTrace();
//			}
		}
	}

	static class ThreadB implements Runnable {

		private String name;

		public ThreadB(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println("Thread: " + name + " \t");
			LightSubscription subscription = LightSubscription.getSubscription(new AppProps(REGISTRY_LOCAL));
//			try {
//				subscription.createLightPath();
//				subscription.createPath("/test");
//				subscription.createPath("/test/22222");
//			} catch (KeeperException | InterruptedException e1) {
//				e1.printStackTrace();
//			}
		}
	}

	static class ThreadC implements Runnable {

		private String name;

		public ThreadC(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println("Thread: " + name + " \t");
			LightSubscription subscription = LightSubscription.getSubscription(new AppProps(REGISTRY_LOCAL));
//			try {
//				subscription.createLightPath();
//				subscription.createPath("/test");
//				subscription.createPath("/test/33333");
//			} catch (KeeperException | InterruptedException e1) {
//				e1.printStackTrace();
//			}
		}
	}

}
