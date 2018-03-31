package com.haiyiyang.light.protocol;

import java.io.Serializable;
import java.nio.ByteBuffer;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.serialize.SerializerContext;

public class PayloadMessage implements Serializable {

	private static final long serialVersionUID = -1L;

	private String requestId;
	private String serviceName;
	private String method;
	private Class<?>[] paramsTypes;
	private String clientIP;
	private String clientAppName;
	private String datetime;
	private String signature;

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	public Class<?>[] getParamType() {
		return paramsTypes;
	}

	public void setParamType(Class<?>[] paramType) {
		this.paramsTypes = paramType;
	}

	public ByteBuffer serialize(Object obj, SerializerContext context) {
		int len = this.serviceName.length() + 4 + this.method.length() + 4 + this.requestId.length() + 4
				+ this.clientIP.length() + 4 + this.clientAppName.length() + 4 + this.datetime.length() + 4
				+ this.signature.length() + 4;
		len = len + 4;
		if (paramsTypes != null) {
			for (Class<?> param : paramsTypes) {
				len = len + param.toString().length() + 4;
			}
		}

		ByteBuffer buffer = ByteBuffer.allocate(len);
		buffer.putInt(serviceName.length()).put(serviceName.getBytes(LightConstants.CHARSET_UTF8));
		buffer.putInt(method.length()).put(method.getBytes(LightConstants.CHARSET_UTF8));
		buffer.putInt(requestId.length()).put(requestId.getBytes(LightConstants.CHARSET_UTF8));
		buffer.putInt(clientIP.length()).put(clientIP.getBytes(LightConstants.CHARSET_UTF8));
		buffer.putInt(clientAppName.length()).put(clientAppName.getBytes(LightConstants.CHARSET_UTF8));
		buffer.putInt(datetime.length()).put(datetime.getBytes(LightConstants.CHARSET_UTF8));
		buffer.putInt(signature.length()).put(signature.getBytes(LightConstants.CHARSET_UTF8));
		if (paramsTypes == null) {
			buffer.putInt(0);
		} else {
			buffer.putInt(paramsTypes.length);
			for (Class<?> param : paramsTypes) {
				buffer.putInt(param.getName().length());
				buffer.put(param.getName().getBytes(LightConstants.CHARSET_UTF8));
			}
		}
		buffer.flip();
		return buffer;
	}

	public PayloadMessage deserialize(ByteBuffer buffer, SerializerContext context) {
		int len = buffer.getInt();
		byte[] data = new byte[len];
		buffer.get(data);
		this.setServiceName(new String(data, LightConstants.CHARSET_UTF8));

		len = buffer.getInt();
		data = new byte[len];
		buffer.get(data);
		this.setMethod(new String(data, LightConstants.CHARSET_UTF8));

		len = buffer.getInt();
		data = new byte[len];
		buffer.get(data);
		this.setRequestId(new String(data, LightConstants.CHARSET_UTF8));

		len = buffer.getInt();
		data = new byte[len];
		buffer.get(data);
		this.setClientIP(new String(data, LightConstants.CHARSET_UTF8));

		len = buffer.getInt();
		data = new byte[len];
		buffer.get(data);
		this.setClientAppName(new String(data, LightConstants.CHARSET_UTF8));

		len = buffer.getInt();
		data = new byte[len];
		buffer.get(data);
		this.setDatetime(new String(data, LightConstants.CHARSET_UTF8));

		len = buffer.getInt();
		data = new byte[len];
		buffer.get(data);
		this.setSignature(new String(data, LightConstants.CHARSET_UTF8));

		if (buffer.hasRemaining()) {
			int paramConut = buffer.getInt();
			if (paramConut != 0) {
				Class<?>[] paramTypeSer = new Class[paramConut];
				for (int i = 0; i < paramConut; i++) {
					len = buffer.getInt();
					data = new byte[len];
					buffer.get(data);
					try {
						String paramType = new String(data, LightConstants.CHARSET_UTF8);
						if ("int".equals(paramType)) {
							paramTypeSer[i] = int.class;
						} else if ("double".equals(paramType)) {
							paramTypeSer[i] = double.class;
						} else if ("byte".equals(paramType)) {
							paramTypeSer[i] = byte.class;
						} else if ("short".equals(paramType)) {
							paramTypeSer[i] = short.class;
						} else if ("float".equals(paramType)) {
							paramTypeSer[i] = float.class;
						} else if ("long".equals(paramType)) {
							paramTypeSer[i] = long.class;
						} else if ("char".equals(paramType)) {
							paramTypeSer[i] = char.class;
						} else if ("boolean".equals(paramType)) {
							paramTypeSer[i] = boolean.class;
						}

						else if ("[I".equals(paramType)) {
							paramTypeSer[i] = int[].class;
						} else if ("[D".equals(paramType)) {
							paramTypeSer[i] = double[].class;
						} else if ("[B".equals(paramType)) {
							paramTypeSer[i] = byte[].class;
						} else if ("[S".equals(paramType)) {
							paramTypeSer[i] = short[].class;
						} else if ("[F".equals(paramType)) {
							paramTypeSer[i] = float[].class;
						} else if ("[L".equals(paramType)) {
							paramTypeSer[i] = long[].class;
						} else if ("[C".equals(paramType)) {
							paramTypeSer[i] = char[].class;
						} else if ("[B".equals(paramType)) {
							paramTypeSer[i] = boolean[].class;
						} else {
							paramTypeSer[i] = Class.forName(new String(data, LightConstants.CHARSET_UTF8));
						}

					} catch (ClassNotFoundException e) {
					}
				}
				this.setParamType(paramTypeSer);
			}
		}
		return this;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Class<?>[] getParamsTypes() {
		return paramsTypes;
	}

	public void setParamsTypes(Class<?>[] paramsTypes) {
		this.paramsTypes = paramsTypes;
	}

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

}
