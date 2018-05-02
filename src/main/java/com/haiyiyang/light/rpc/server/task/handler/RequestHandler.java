package com.haiyiyang.light.rpc.server.task.handler;

import com.haiyiyang.light.context.LightContext;
import com.haiyiyang.light.meta.props.LightProps;
import com.haiyiyang.light.protocol.ProtocolPacket;
import com.haiyiyang.light.rpc.server.task.TaskExecutor;
import com.haiyiyang.light.rpc.server.task.TaskQueue;

public class RequestHandler extends Thread {

	private LightProps lightProps;

	private static RequestHandler requestHandler;

	private RequestHandler() {
		this.setName("Request Handler Thread.");
		this.lightProps = LightContext.getContext().getLightAppMeta().getLightProps();
	}

	public synchronized static void handle() {
		if (requestHandler == null) {
			requestHandler = new RequestHandler();
			requestHandler.start();
		}
	}

	@Override
	public void run() {
		boolean threadPoolIsExecute = false;
		ProtocolPacket protocolPacket = null;
		while (true) {
			try {
				if (!threadPoolIsExecute && protocolPacket != null) {
					Thread.sleep(1);
					threadPoolIsExecute = execute(protocolPacket);
				} else {
					protocolPacket = TaskQueue.SINGLETON().get();
					threadPoolIsExecute = execute(protocolPacket);
				}

			} catch (Throwable e) {
				// TODO
			}

		}
	}

	private boolean execute(ProtocolPacket protocolPacket) {
		boolean result = true;
		if (protocolPacket == null) {
			return result;
		}
		if ((System.currentTimeMillis() - protocolPacket.getStartTime()) < lightProps.getTimeout()) {
			return TaskExecutor.SINGLETON(lightProps).execute(new ResponseHandler(protocolPacket));
		} else {
			if (protocolPacket != null) {
				// TODO
			}
		}
		return result;
	}

}
