package com.haiyiyang.light.serialize;

import java.nio.ByteBuffer;

public class ProtoBufSerializer implements Serializer {

	private static ProtoBufSerializer PROTOBUF_SERIALIZER = new ProtoBufSerializer();

	private ProtoBufSerializer() {
	}

	public static ProtoBufSerializer SINGLETON() {
		return PROTOBUF_SERIALIZER;
	}

	public ByteBuffer serialize(Object obj, SerializerContext context) {
		return null; // TODO
	}

	public Object deserialize(ByteBuffer buffer, SerializerContext context) {
		return null; // TODO
	}
}