package com.haiyiyang.light.utils;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

public class LightUtils {

	public static Object getService(Object obj) throws Exception {
		if (AopUtils.isAopProxy(obj) && obj instanceof Advised) {
			Advised advised = (Advised) obj;
			obj = advised.getTargetSource().getTarget();
			return getService(obj);
		}
		return obj;
	}

}
