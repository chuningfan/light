package com.haiyiyang.light.test;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.haiyiyang.light.rpc.response.ResponseFuture;

public class Tester {

	private static final Logger LOGGER = LoggerFactory.getLogger(Tester.class);

	static class FakeTicker extends Ticker {

		private final AtomicLong nanos = new AtomicLong();

		/** Advances the ticker value by {@code time} in {@code timeUnit}. */
		public FakeTicker advance(long time, TimeUnit timeUnit) {
			nanos.addAndGet(timeUnit.toNanos(time));
			return this;
		}

		@Override
		public long read() {
			long value = nanos.getAndAdd(0);
			System.out.println("is called " + value);
			return value;
		}
	}

	private static FakeTicker ticker = new FakeTicker();

	public static LoadingCache<Integer, Future<?>> FUTURE_CACHE = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(3, TimeUnit.SECONDS).ticker(ticker).build(new CacheLoader<Integer, Future<?>>() {
				public Future<?> load(Integer integer) {
					return null;
				}
			});

	public static void main(String[] args) {
		LOGGER.debug("Start testing.");
		FUTURE_CACHE.put(1, new ResponseFuture<>(true));
		LOGGER.info(FUTURE_CACHE.getUnchecked(1).toString());
		ticker.advance(1, TimeUnit.SECONDS);
		LOGGER.info(String.valueOf(FUTURE_CACHE.getUnchecked(1)));
		ticker.advance(2, TimeUnit.SECONDS);
		LOGGER.info(String.valueOf(FUTURE_CACHE.getIfPresent(1)));
	}

}
