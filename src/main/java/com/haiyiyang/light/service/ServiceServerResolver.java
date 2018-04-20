package com.haiyiyang.light.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.rpc.server.IpPortGroupWeight;

public class ServiceServerResolver {
	private static Map<String, IpPortGroupWeight> LATEST_IPGW = new ConcurrentHashMap<>();

	public static IpPortGroupWeight getServer(String serviceName) {
		return null;
	}

	private static int GCD(int m, int n) {
		int r;
		while (m % n != 0) {
			r = n;
			n = m % n;
			m = r;
		}
		return n;
	}

	private static int GCD(List<IpPortGroupWeight> ipgw) {
		int r = 0, weight = 0;
		Iterator<IpPortGroupWeight> iter = ipgw.iterator();
		while (iter.hasNext()) {
			weight = iter.next().getWeight();
			if (weight != 0) {
				r = GCD(r, weight);
			}
		}
		return r;
	}

	private static byte maxWeight(List<IpPortGroupWeight> entryList) {
		byte maxWeight = 0;
		Iterator<IpPortGroupWeight> iter = entryList.iterator();
		while (iter.hasNext()) {
			byte curWeight = iter.next().getWeight();
			if (curWeight > maxWeight)
				maxWeight = curWeight;
		}
		return maxWeight;
	}

	public static IpPortGroupWeight getService(String serviceName, List<IpPortGroupWeight> entrieList) {
		IpPortGroupWeight lastIpgw = LATEST_IPGW.get(serviceName);
		int index = -1;
		byte currentWeight = 0;
		if (lastIpgw != null) {
			index = entrieList.indexOf(lastIpgw);
			currentWeight = lastIpgw.getWeight();
		}
		int gcd = GCD(entrieList);
		byte maxWeight = maxWeight(entrieList);
		int size = entrieList.size();
		while (true) {
			index = (index + 1) % size;
			if (index == 0) {
				currentWeight -= gcd;
				if (currentWeight <= 0) {
					currentWeight = maxWeight;
					if (currentWeight == 0)
						return null;
				}
			}
			IpPortGroupWeight ipgw = entrieList.get(index);
			if (ipgw.getWeight() >= currentWeight) {
				LATEST_IPGW.put(serviceName, new IpPortGroupWeight(ipgw.getIp(), ipgw.getPort(), currentWeight));
				return ipgw;
			}
		}
	}
}
