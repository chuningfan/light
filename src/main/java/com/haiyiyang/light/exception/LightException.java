package com.haiyiyang.light.exception;

public class LightException extends RuntimeException {

	private static final long serialVersionUID = 2356666188593679816L;

	private final byte code;
	private final String message;
	private static final String codePrefix = "code: ";
	private static final String messagePrefix = " | message: ";

	public byte getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public LightException(byte code, String message) {
		super(getCauseMessage(code, message));
		this.code = code;
		this.message = message;
	}

	public LightException(byte code, String message, Throwable cause) {
		super(getCauseMessage(code, message), cause);
		this.code = code;
		this.message = message;
	}

	private static String getCauseMessage(byte code, String message) {
		return new StringBuilder(codePrefix).append(code).append(messagePrefix).append(message).toString();
	}

	public static enum Code {

		UNDEFINED((byte) -1), NOSERVICE((byte) -2), ZKERROR((byte) -3);

		private byte value;

		private Code(byte value) {
			this.value = value;
		}

		public byte getValue() {
			return value;
		}

		public void setValue(byte value) {
			this.value = value;
		}
	}

}
