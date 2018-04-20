package com.haiyiyang.light.serialization.json;

import java.nio.ByteBuffer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.haiyiyang.light.serialization.Serializer;
import com.haiyiyang.light.serialization.SerializerContext;
import com.haiyiyang.light.serialization.SerializerFactory;

public class JsonSerializer implements Serializer {

	private static JsonSerializer JSON_SERIALIZER = new JsonSerializer();

	private JsonSerializer() {
	}

	public static JsonSerializer SINGLETON() {
		return JSON_SERIALIZER;
	}

	public ByteBuffer serialize(Object obj, SerializerContext context) {
		return ByteBuffer.wrap(JSON.toJSONBytes(obj, SerializerFeature.WriteClassName));
	}

	public Object deserialize(ByteBuffer buffer, SerializerContext context) {
		if (context.isRequest()) {
			if (context.getParamsTypes().length == 1) {
				return JSON.parseArray(SerializerFactory.getString(buffer.array()), context.getParamsTypes()[0])
						.toArray();
			} else {
				return JSON.parseArray(SerializerFactory.getString(buffer.array()), context.getParamsTypes()).toArray();
			}
		} else {
			return JSON.parseObject(buffer.array(), context.getReturnType());
		}
	}
}