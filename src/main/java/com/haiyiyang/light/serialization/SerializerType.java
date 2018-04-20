package com.haiyiyang.light.serialization;

import com.haiyiyang.light.constant.LightConstants;

public enum SerializerType {

	PROTOBUF(LightConstants.BYTE1), JSON(LightConstants.BYTE2);

	private byte value;

	SerializerType(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}
}
