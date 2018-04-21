package com.haiyiyang.light.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.service.entry.ServiceEntry;

public class ServerResolver {
	private static Map<String, ServiceEntry> LATEST_SERVICE_ENTRY = new ConcurrentHashMap<>();

	public static ServiceEntry getServer(String serviceName, Byte group) {
		List<ServiceEntry> list = LightService.SINGLETON().subscribeService(serviceName);
		List<ServiceEntry> serviceEntryList = null;
		if (group == null) {
			serviceEntryList = list;
		} else {
			serviceEntryList = new ArrayList<>(list.size());
			for (ServiceEntry serviceEntry : list) {
				if (serviceEntry.getGroup() == group.byteValue()) {
					serviceEntryList.add(serviceEntry);
				}
			}
		}
		return getService(serviceName, serviceEntryList);
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

	private static int GCD(List<ServiceEntry> serviceEntry) {
		int r = 0, weight = 0;
		Iterator<ServiceEntry> iter = serviceEntry.iterator();
		while (iter.hasNext()) {
			weight = iter.next().getWeight();
			if (weight != 0) {
				r = GCD(r, weight);
			}
		}
		return r;
	}

	private static byte maxWeight(List<ServiceEntry> entryList) {
		byte maxWeight = 0;
		Iterator<ServiceEntry> iter = entryList.iterator();
		while (iter.hasNext()) {
			byte curWeight = iter.next().getWeight();
			if (curWeight > maxWeight)
				maxWeight = curWeight;
		}
		return maxWeight;
	}

	public static ServiceEntry getService(String serviceName, List<ServiceEntry> entrieList) {
		ServiceEntry latestServiceEntry = LATEST_SERVICE_ENTRY.get(serviceName);
		int index = -1;
		byte currentWeight = 0;
		if (latestServiceEntry != null) {
			index = entrieList.indexOf(latestServiceEntry);
			currentWeight = latestServiceEntry.getWeight();
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
			ServiceEntry ipgw = entrieList.get(index);
			if (ipgw.getWeight() >= currentWeight) {
				LATEST_SERVICE_ENTRY.put(serviceName, new ServiceEntry(ipgw.getIpPort(), currentWeight));
				return ipgw;
			}
		}
	}
}
