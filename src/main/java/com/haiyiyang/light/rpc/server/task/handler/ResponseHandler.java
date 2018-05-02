package com.haiyiyang.light.rpc.server.task.handler;

import com.haiyiyang.light.protocol.ProtocolPacket;
import com.haiyiyang.light.serialization.SerializerFactory;

public class ResponseHandler implements Runnable {

	private ProtocolPacket protocolPacket;

	public ResponseHandler(ProtocolPacket protocolPacket) {
		this.protocolPacket = protocolPacket;
	}

	@Override
	public void run() {
		byte serializerType = protocolPacket.getSerializerType();
		if (serializerType == SerializerFactory.SERIALIZER_JSON) {

		} else if (serializerType == SerializerFactory.SERIALIZER_PROTOBUF) {

		}
	}

}
