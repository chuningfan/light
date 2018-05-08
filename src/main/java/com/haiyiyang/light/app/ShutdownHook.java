package com.haiyiyang.light.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.context.LightContext;
import com.haiyiyang.light.rpc.server.LightRpcServer;
import com.haiyiyang.light.service.LightService;

public class ShutdownHook extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownHook.class);

	private ShutdownHook() {

	}

	private static ShutdownHook SHUTDOWN_HOOK;

	public synchronized static void hook() {
		if (SHUTDOWN_HOOK == null) {
			Thread thread = new ShutdownHook();
			Runtime.getRuntime().addShutdownHook(thread);
		}
	}

	public void run() {
		LOGGER.info("ShutdownHook started.");
		try {
			LightService.doUnpublishLightService();
			Thread.sleep(30 * 1000);
			LightRpcServer.SINGLETON().stop();
		} catch (Throwable e) {
			LOGGER.info("ShutdownHook run failed, exception: {}", e.getMessage());
		} finally {
			if (LightContext.getContext() != null) {
				LightContext.getContext().close();
			}
		}
	}
}
