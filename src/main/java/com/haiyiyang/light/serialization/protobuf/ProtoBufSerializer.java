package com.haiyiyang.light.serialization.protobuf;

import java.nio.ByteBuffer;

import com.haiyiyang.light.serialization.Serializer;
import com.haiyiyang.light.serialization.SerializerContext;

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