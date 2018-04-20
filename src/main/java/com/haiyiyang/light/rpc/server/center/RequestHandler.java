package com.haiyiyang.light.rpc.server.center;

import com.haiyiyang.light.meta.props.LightProps;
import com.haiyiyang.light.protocol.ProtocolPacket;

public class RequestHandler implements Runnable {

	private LightProps lightProps;

	public RequestHandler(LightProps lightProps) {
		this.lightProps = lightProps;
	}

	@Override
	public void run() {
		boolean threadPoolIsExecute = false;
		LightRequest lightRequest = null;
		while (true) {
			try {
				if (!threadPoolIsExecute && lightRequest != null) {
					Thread.sleep(1);
					threadPoolIsExecute = execute(lightRequest);

				} else {
					lightRequest = RequestQueue.SINGLETON().get();
					threadPoolIsExecute = execute(lightRequest);
				}

			} catch (Throwable e) {
				// TODO
			}

		}
	}

	private boolean execute(LightRequest lightRequest) {
		boolean result = true;
		if (lightRequest == null) {
			return result;
		}
		ProtocolPacket packet = lightRequest.getProtocolPacket();
		if (packet != null && ((System.currentTimeMillis() - packet.getStartTime()) < lightProps.getTimeout())) {
			return TaskExecutor.SINGLETON(lightProps).execute(new ResponseHandler(lightRequest));
		} else {
			if (packet != null) {
				// TODO
			}
		}
		return result;
	}

}
