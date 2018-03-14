package com.haiyiyang.light.test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Tester {

	private static final Map<Integer, Integer> MAP = new ConcurrentHashMap<>(2);

	public static void main(String[] args) {
		Tester1.test1();
		MAP.put(1, 1);
		MAP.put(2, 2);
		MAP.put(3, 3);
		MAP.put(4, 4);
		System.out.println(MAP.get(1));
		System.out.println(MAP.get(1));
		System.out.println(MAP.get(2));
		System.out.println(System.getProperty("user.home"));

	}

	public static void test() {
		String callerClassName = new Exception().getStackTrace()[2].getClassName();
		System.out.println(callerClassName);
	}

}

class Tester1 {

	public static void test1() {
		System.out.println(1);
		Tester3.test3();
	}
}

class Tester3 {

	public static void test3() {
		System.out.println(3);
		Tester.test();
	}
}