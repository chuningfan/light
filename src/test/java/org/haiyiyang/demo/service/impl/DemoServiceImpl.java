package org.haiyiyang.demo.service.impl;

import org.haiyiyang.demo.proto.DemoContext.Context;
import org.haiyiyang.demo.proto.DemoSearch.Result;
import org.haiyiyang.demo.proto.DemoSearch.SearchRequest;
import org.haiyiyang.demo.proto.DemoSearch.SearchResponse;
import org.haiyiyang.demo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.haiyiyang.light.service.annotation.IAmALightService;

@IAmALightService
public class DemoServiceImpl implements DemoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DemoServiceImpl.class);

	@Override
	public String helloWord(String name) {
		return "Hello " + name + ", to the World.";
	}

	@Override
	public String welcome(String firstName, String lastName) {
		// TODO Auto-generated method stub
		return "Welcome " + firstName + ", " + lastName;
	}

	@Override
	public SearchResponse search(Context cxt, SearchRequest request) {
		LOGGER.info("cxt.getAgencyId() >>" + cxt.getAgencyId());
		LOGGER.info("cxt.getAgencyName() >>" + cxt.getAgencyName());
		LOGGER.info("request.getQuery() >>" + request.getQuery());
		SearchResponse sr = SearchResponse.newBuilder()
				.setResults(0, Result.newBuilder().setTitle(request.getQuery()).build()).build();
		return sr;
	}

}
