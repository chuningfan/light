package com.haiyiyang.light.server.task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.haiyiyang.light.server.RequestMessage;

public class MessageQueue {

	private static volatile MessageQueue MESSAGE_QUEUE = new MessageQueue();

	private BlockingQueue<RequestMessage> BLOCKING_QUEUE = new LinkedBlockingQueue<RequestMessage>(100);

}