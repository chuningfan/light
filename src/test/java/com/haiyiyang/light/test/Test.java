package com.haiyiyang.light.test;

import com.haiyiyang.light.constant.LightConstants;

public class Test {

	public static void main(String[] args) {
		String serviceName = "com.haiyiyang1.prod.abc.TestImpl";
		String domainPackage = "com.haiyiyang";
		if (serviceName.indexOf(domainPackage) == 0) {
			int index = serviceName.indexOf(LightConstants.DOT, domainPackage.length() + 1);
			if (index == -1) {
				index = serviceName.length();
			}
			String result = serviceName.substring(domainPackage.length() + 1, index);
			System.out.println(result);
		}
		
	}

}
