package com.haiyiyang.light.rpc.server.center;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {

	private static volatile RequestQueue MESSAGE_QUEUE = new RequestQueue();

	private BlockingQueue<LightRequest> BLOCKING_QUEUE = new LinkedBlockingQueue<LightRequest>(100);

	private RequestQueue() {
	}

	public static RequestQueue SINGLETON() {
		return MESSAGE_QUEUE;
	}

	public boolean add(LightRequest lightRequest) {
		return BLOCKING_QUEUE.offer(lightRequest);

	}

	public LightRequest get() {
		try {
			return BLOCKING_QUEUE.take();
		} catch (InterruptedException e) {
			// TODO
		}
		return null;
	}

	public int getSize() {
		return BLOCKING_QUEUE.size();
	}
}