package com.haiyiyang.light.service.routing;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.haiyiyang.light.service.entry.LightServiceEntry;

public class WRRRouter {
	private static Map<String, LightServiceEntry> LATEST_ENTRIES = new ConcurrentHashMap<>();

	private static int GCD(int m, int n) {
		int r;
		while (m % n != 0) {
			r = n;
			n = m % n;
			m = r;
		}
		return n;
	}

	private static int GCD(List<LightServiceEntry> entryList) {
		int r = 0, weight = 0;
		Iterator<LightServiceEntry> iter = entryList.iterator();
		while (iter.hasNext()) {
			weight = iter.next().getWeight();
			if (weight != 0) {
				r = GCD(r, weight);
			}
		}
		return r;
	}

	private static int maxWeight(List<LightServiceEntry> entryList) {
		int maxWeight = 0;
		Iterator<LightServiceEntry> iter = entryList.iterator();
		while (iter.hasNext()) {
			int curWeight = iter.next().getWeight();
			if (curWeight > maxWeight)
				maxWeight = curWeight;
		}
		return maxWeight;
	}

	public static LightServiceEntry getService(String serviceName, List<LightServiceEntry> entrieList) {
		LightServiceEntry lastEntry = LATEST_ENTRIES.get(serviceName);
		int index = -1;
		int currentWeight = 0;
		if (lastEntry != null) {
			index = entrieList.indexOf(lastEntry);
			currentWeight = lastEntry.getWeight();
		}
		int gcd = GCD(entrieList);
		int maxWeight = maxWeight(entrieList);
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
			LightServiceEntry serviceEntry = entrieList.get(index);
			if (serviceEntry.getWeight() >= currentWeight) {
				LightServiceEntry service = (LightServiceEntry) serviceEntry.clone();
				service.setWeight(currentWeight);
				LATEST_ENTRIES.put(service.getServiceName(), service);
				return serviceEntry;
			}
		}
	}
}
