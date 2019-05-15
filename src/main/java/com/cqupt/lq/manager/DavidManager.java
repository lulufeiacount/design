package com.cqupt.lq.manager;

import com.cqupt.lq.po.David;

import java.util.*;

public class DavidManager {
	private static DavidManager davidManager = new DavidManager();
	private Set<David> davidSet;

	public static DavidManager getDavidManager(){
		return davidManager;
	}

	public Set<David> getDavidSet() {
		return davidSet;
	}

	private DavidManager(){
		davidSet = new HashSet<>();
	}

	public List<David> getSortedDavidList(){
		List<David> davidList = new ArrayList<>(davidSet);
		Collections.sort(davidList);
		return davidList;
	}
}
