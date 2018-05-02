package com.haiyiyang.light.rpc.server.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.haiyiyang.light.protocol.ProtocolPacket;

public class TaskQueue {

	private static volatile TaskQueue TASK_QUEUE = new TaskQueue();

	private BlockingQueue<ProtocolPacket> BLOCKING_QUEUE = new LinkedBlockingQueue<ProtocolPacket>(100);

	private TaskQueue() {
	}

	public static TaskQueue SINGLETON() {
		return TASK_QUEUE;
	}

	public boolean add(ProtocolPacket protocolPacket) {
		return BLOCKING_QUEUE.offer(protocolPacket);

	}

	public ProtocolPacket get() {
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