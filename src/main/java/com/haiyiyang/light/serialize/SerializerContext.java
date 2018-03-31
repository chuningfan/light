package com.haiyiyang.light.serialize;

public class SerializerContext {

	private boolean isRequest = true;

	private Class<?>[] paramsTypes;

	private Class<?> returnType;

	public SerializerContext(boolean isRequest, Class<?>[] paramsTypes, Class<?> returnType) {
		super();
		this.isRequest = isRequest;
		this.paramsTypes = paramsTypes;
		this.returnType = returnType;
	}

	public boolean isRequest() {
		return isRequest;
	}

	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}

	public Class<?>[] getParamsTypes() {
		return paramsTypes;
	}

	public void setParamsTypes(Class<?>[] paramsTypes) {
		this.paramsTypes = paramsTypes;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}

}
