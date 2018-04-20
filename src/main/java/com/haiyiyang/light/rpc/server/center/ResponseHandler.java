package com.haiyiyang.light.rpc.server.center;

import com.haiyiyang.light.serialization.SerializerFactory;

public class ResponseHandler implements Runnable {

	private LightRequest lightRequest;

	public ResponseHandler(LightRequest lightRequest) {
		this.lightRequest = lightRequest;
	}

	@Override
	public void run() {
		byte serializerType = lightRequest.getProtocolPacket().getSerializerType();
		if (serializerType == SerializerFactory.SERIALIZER_JSON) {

		} else if (serializerType == SerializerFactory.SERIALIZER_PROTOBUF) {

		}
	}

}
