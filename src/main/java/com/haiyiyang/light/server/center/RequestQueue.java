package com.haiyiyang.light.server.center;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class RequestQueue {

	private static volatile RequestQueue MESSAGE_QUEUE = new RequestQueue();

	private BlockingQueue<LightRequest> BLOCKING_QUEUE = new LinkedBlockingQueue<LightRequest>(100);

}