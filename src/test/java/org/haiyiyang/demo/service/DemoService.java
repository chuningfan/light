package org.haiyiyang.demo.service;

import org.haiyiyang.demo.proto.DemoContext;
import org.haiyiyang.demo.proto.DemoSearch;

public interface DemoService {

	public String helloWord(String name);

	public String welcome(String firstName, String lastName);

	public DemoSearch.SearchResponse search(DemoContext.Context cxt, DemoSearch.SearchRequest request);
}
