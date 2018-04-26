package com.haiyiyang.light.exception;

public class LightException extends RuntimeException {

	private static final long serialVersionUID = -1L;

	private final String code;
	private final String message;
	private static final String CODE_PREFIX = "LightException > code: ";
	private static final String MESSAGE_PREFIX = " | message: ";

	public String getCode() {
		return code;
	}

	public LightException(Code code) {
		super(getCauseMessage(code.name(), null));
		this.code = code.name();
		this.message = null;
	}

	public LightException(Code code, String message) {
		super(getCauseMessage(code.name(), message));
		this.code = code.name();
		this.message = message;
	}

	public LightException(Code code, String message, Throwable cause) {
		super(getCauseMessage(code.name(), message), cause);
		this.code = code.name();
		this.message = message;
	}

	private static String getCauseMessage(String code, String message) {
		return new StringBuilder(CODE_PREFIX).append(code).append(MESSAGE_PREFIX).append(message).toString();
	}

	public static enum Code {
		UNDEFINED(), NO_SERVICE(), ZK_ERROR(), SETTINGS_ERROR();
	}

	@Override
	public String toString() {
		return "LightException [code=" + code + ", message=" + message + "]";
	}

	public static void main(String[] args) {
		try {
			throw new LightException(Code.SETTINGS_ERROR);
		} catch (LightException e) {
			System.out.println(e.getMessage());
		}
	}

}
