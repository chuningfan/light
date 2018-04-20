package com.haiyiyang.light.rpc.server;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.exception.LightException;

public class LightConfig {

	private static Logger LOGGER = LoggerFactory.getLogger(LightConfig.class);
	private static final String CONFIG_SERVER_URL = "http://config.haiyiyang.com";

	public static String getConfigServer() throws LightException {
		String result = null;
		CloseableHttpResponse response = null;
		try {
			response = HttpClients.createDefault().execute(new HttpGet(CONFIG_SERVER_URL));
		} catch (IOException e) {
			LOGGER.info("Light config server is not available: {}", CONFIG_SERVER_URL);
		}
		if (response != null) {
			try {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream is = entity.getContent();
					try {
						StringBuffer out = new StringBuffer();
						byte[] b = new byte[4096];
						for (int n; (n = is.read(b)) != -1;) {
							out.append(new String(b, 0, n));
						}
						result = out.toString();
					} finally {
						is.close();
					}
				}
			} catch (UnsupportedOperationException | IOException e) {
				LOGGER.info("Parse Light config server URL error.");
			} finally {
				try {
					response.close();
				} catch (IOException e) {
					LOGGER.info("Close Http Response error.");
				}
			}
		}
		if (result == null) {
			throw new LightException(LightException.Code.UNDEFINED, "Light config server URL is invalid.");
		}
		LOGGER.info("Light config server URL: {}", result);
		return result;
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		String config = getConfigServer();
		System.out.println(config);
	}

}
