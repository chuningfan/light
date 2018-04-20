package com.haiyiyang.light.serialization;

import java.nio.ByteBuffer;

public interface Serializer {

	public ByteBuffer serialize(Object obj, SerializerContext context);

	public Object deserialize(ByteBuffer buffer, SerializerContext context);

}
