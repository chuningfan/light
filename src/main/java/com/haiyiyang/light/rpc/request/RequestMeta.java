package com.haiyiyang.light.rpc.request;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.utils.LightClassUtils;

public class RequestMeta implements Serializable {

	private static final long serialVersionUID = -1L;
	private static final int FIELDS_COUNT = 7;

	private String requestId;
	private String serviceName;
	private String method;
	private String clientIP;
	private String clientAppName;
	private String datetime;
	private Class<?>[] paramsTypes;

	public RequestMeta() {
	}

	public RequestMeta(String requestId, String serviceName, String method, Class<?>[] paramsTypes, String clientIP,
			String clientAppName, String datetime) {
		super();
		this.requestId = requestId;
		this.serviceName = serviceName;
		this.method = method;
		this.paramsTypes = paramsTypes;
		this.clientIP = clientIP;
		this.clientAppName = clientAppName;
		this.datetime = datetime;
	}

	public ByteBuffer serialize() {
		int len = 0;
		List<String> fieldsValues = Lists.newArrayList(requestId, serviceName, method, clientIP, clientAppName,
				datetime);
		for (int i = 0; i < FIELDS_COUNT; i++) {
			len += (4 + getFieldLength(fieldsValues.get(i)));
		}
		len = len + 4;
		if (paramsTypes != null) {
			for (Class<?> param : paramsTypes) {
				len += (4 + param.getName().length());
			}
		}
		ByteBuffer buffer = ByteBuffer.allocate(len);
		for (int j = 0; j < FIELDS_COUNT; j++) {
			buffer.putInt(getFieldLength(fieldsValues.get(j))).put(getFieldValue(fieldsValues.get(j)));
		}
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

	public RequestMeta deserialize(ByteBuffer buffer) {
		List<String> fieldsValues = new ArrayList<>(FIELDS_COUNT);
		byte[] data;
		for (int i = 0; i < FIELDS_COUNT; i++) {
			data = new byte[buffer.getInt()];
			buffer.get(data);
			fieldsValues.add(new String(data, LightConstants.CHARSET_UTF8));
		}
		this.requestId = fieldsValues.get(0);
		this.serviceName = fieldsValues.get(1);
		this.method = fieldsValues.get(2);
		this.clientIP = fieldsValues.get(3);
		this.clientAppName = fieldsValues.get(4);
		this.datetime = fieldsValues.get(5);
		if (buffer.hasRemaining()) {
			int paramConut = buffer.getInt();
			if (paramConut != 0) {
				Class<?>[] classArray = new Class[paramConut];
				for (int j = 0; j < paramConut; j++) {
					data = new byte[buffer.getInt()];
					buffer.get(data);
					classArray[j] = LightClassUtils.forName(new String(data, LightConstants.CHARSET_UTF8));
				}
				this.paramsTypes = classArray;
			}
		}
		return this;
	}

	private static int getFieldLength(String field) {
		return field == null ? 0 : field.length();
	}

	private static byte[] getFieldValue(String fieldValue) {
		return fieldValue == null ? new byte[0] : fieldValue.getBytes(LightConstants.CHARSET_UTF8);
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

	public String getClientIP() {
		return clientIP;
	}

	public void setClientIP(String clientIP) {
		this.clientIP = clientIP;
	}

	public String getClientAppName() {
		return clientAppName;
	}

	public void setClientAppName(String clientAppName) {
		this.clientAppName = clientAppName;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public Class<?>[] getParamsTypes() {
		return paramsTypes;
	}

	public void setParamsTypes(Class<?>[] paramsTypes) {
		this.paramsTypes = paramsTypes;
	}

}
