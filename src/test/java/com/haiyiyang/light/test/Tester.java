package com.haiyiyang.light.test;

public class Tester {

	public static void main(String[] args) {
		Tester1.test1();
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