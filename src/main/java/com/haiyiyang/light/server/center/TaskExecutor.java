package com.haiyiyang.light.server.center;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.haiyiyang.light.meta.props.LightProps;

public class TaskExecutor {

	private ThreadPoolExecutor threadPool;

	private static volatile TaskExecutor taskExecutor;

	private TaskExecutor(LightProps lightProps) {
		threadPool = new ThreadPoolExecutor(lightProps.getMinThread(), lightProps.getMaxThread(), 60, TimeUnit.SECONDS,
				new SynchronousQueue<Runnable>(), new ThreadPoolExecutor.AbortPolicy());
	}

	public static TaskExecutor SINGLETON(LightProps lightProps) {
		if (taskExecutor != null) {
			return taskExecutor;
		}
		synchronized (taskExecutor) {
			if (taskExecutor == null) {
				taskExecutor = new TaskExecutor(lightProps);
			}
		}
		return taskExecutor;
	}

	public boolean execute(Runnable runnable) {
		boolean result = true;
		if (runnable != null) {
			try {
				threadPool.execute(runnable);
			} catch (RejectedExecutionException e) {
				result = false;
			}
		}
		return result;
	}
}
