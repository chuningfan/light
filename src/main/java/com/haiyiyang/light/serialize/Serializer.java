package com.haiyiyang.light.serialize;

import java.nio.ByteBuffer;

public interface Serializer {

	public ByteBuffer serialize(Object obj, SerializerContext context);

	public Object deserialize(ByteBuffer buffer, SerializerContext context);

}
