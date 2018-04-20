package com.haiyiyang.light.rpc;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

import com.haiyiyang.light.rpc.response.ResponseFuture;
import com.haiyiyang.light.service.LightService;

public class LightRpcContext {

	private Integer currentPacketId;
	private Map<Integer, ResponseFuture<?>> futureMap = new ConcurrentHashMap<>(8);

	private static final ThreadLocal<LightRpcContext> THREAD_LOCAL = new ThreadLocal<LightRpcContext>() {
		@Override
		protected LightRpcContext initialValue() {
			return new LightRpcContext();
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> Future<T> getFuture(Integer packetId) {
		return (Future<T>) THREAD_LOCAL.get().futureMap.get(packetId);
	}

	public static void setResponseFuture(Integer packetId, ResponseFuture<?> future) {
		THREAD_LOCAL.get().currentPacketId = packetId;
		THREAD_LOCAL.get().futureMap.put(packetId, future);
	}

	public static ResponseFuture<?> getResponseFuture(Integer packetId) {
		return THREAD_LOCAL.get().futureMap.get(packetId);
	}

	@SuppressWarnings("unchecked")
	public static <T> Future<T> getCurrentFuture() {
		return (Future<T>) THREAD_LOCAL.get().futureMap.get(THREAD_LOCAL.get().currentPacketId);
	}

	public <T> Future<T> asyncCall(Object service, Callable<T> callable) {
		try {
			if (LightService.isLocalService(service)) {
				FutureTask<T> futureTask = new FutureTask<T>(callable);
				futureTask.run();
				return futureTask;
			} else {
				callable.call();
				return getCurrentFuture();
			}
		} catch (Exception e) {
			// TODO
		}
		return null;
	}

}
