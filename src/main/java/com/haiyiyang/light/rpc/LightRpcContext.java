package com.haiyiyang.light.rpc;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public class LightRpcContext {

	private Map<String, Future<?>> futureMap = new ConcurrentHashMap<>(8);

	private static final ThreadLocal<LightRpcContext> THREAD_LOCAL = new ThreadLocal<LightRpcContext>() {
		@Override
		protected LightRpcContext initialValue() {
			return new LightRpcContext();
		}
	};

	public static LightRpcContext getContext() {
		return THREAD_LOCAL.get();
	}

	public static void removeContext() {
		THREAD_LOCAL.remove();
	}
	
//	public static <T> Future<T> getFuture() {
//        return (Future<T>) THREAD_LOCAL.get().futureMap.get();
//    }
//
//    public void setFuture(Future<?> future) {
//        this.THREAD_LOCAL = future;
//    }
	
	
	public static void main(String[] args) {
		Object x = new Object();
		System.out.println(x);
		
		Object x1 = new Object();
		System.out.println((Integer)null);
		
	}

}
