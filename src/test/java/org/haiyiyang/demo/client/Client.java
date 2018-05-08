package org.haiyiyang.demo.client;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.haiyiyang.demo.proto.DemoContext.Context;
import org.haiyiyang.demo.proto.DemoSearch.SearchRequest;
import org.haiyiyang.demo.proto.DemoSearch.SearchResponse;
import org.haiyiyang.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.constant.LightConstants;
import com.haiyiyang.light.rpc.LightRpcContext;
import com.haiyiyang.light.service.proxy.LightServiceFactory;

public class Client {

	private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

	public static void main(String[] args) {
		System.setProperty("useLocalProps", LightConstants.STR1);
		DemoService demoService = LightServiceFactory.getService(DemoService.class);
		String helloResult = demoService.helloWord("Agent");
		LOGGER.info("helloResult >>> " + helloResult);

		DemoService asyncDemoService = LightServiceFactory.getAsyncService(DemoService.class);
		Future<String> welcomeFuture = LightRpcContext.getContext().asyncCall(asyncDemoService, new Callable<String>() {
			@Override
			public String call() throws Exception {
				return asyncDemoService.welcome("San", "Zhang");
			}
		});
		String welcomeResult = demoService.welcome("Jack", "Ma");
		LOGGER.info("welcomeResult >>> " + welcomeResult);
		try {
			String asyncWelcomeResult = welcomeFuture.get();
			LOGGER.info("asyncWelcomeResult >>> " + asyncWelcomeResult);
		} catch (InterruptedException e) {
			LOGGER.error("Calling async welcome failed >>> " + e.getMessage());
			e.printStackTrace();
		} catch (ExecutionException e) {
			LOGGER.error("Calling async welcome failed >>> " + e.getMessage());
			e.printStackTrace();
		}

		Context cxt = Context.newBuilder().setAgencyId(1).setAgencyName("HYYF").build();
		SearchRequest request1 = SearchRequest.newBuilder().setQuery("ASYNC-HYY").build();

		Future<SearchResponse> searchFuture = LightRpcContext.getContext().asyncCall(asyncDemoService,
				new Callable<SearchResponse>() {
					@Override
					public SearchResponse call() throws Exception {
						return asyncDemoService.search(cxt, request1);
					}
				});

		SearchRequest request2 = SearchRequest.newBuilder().setQuery("HYY").build();
		SearchResponse response = demoService.search(cxt, request2);
		LOGGER.info("Index 0 of search result's title >>> " + response.getResults(0).getTitle());
		try {
			SearchResponse asyncSearchResponse = searchFuture.get();
			LOGGER.info(
					"Async ---> Index 0 of search result's title >>> " + asyncSearchResponse.getResults(0).getTitle());
		} catch (InterruptedException e) {
			LOGGER.error("Calling async search failed >>> " + e.getMessage());
			e.printStackTrace();
		} catch (ExecutionException e) {
			LOGGER.error("Calling async search failed >>> " + e.getMessage());
			e.printStackTrace();
		}
	}
}
