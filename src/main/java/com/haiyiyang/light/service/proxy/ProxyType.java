package com.haiyiyang.light.service.proxy;

import com.haiyiyang.light.constant.LightConstants;

public enum ProxyType {

	JDK(LightConstants.BYTE1), CGLIB(LightConstants.BYTE2);

	private byte value;

	ProxyType(byte value) {
		this.value = value;
	}

	public byte getValue() {
		return this.value;
	}

}
