package com.cqupt.lq.manager;

import com.cqupt.lq.po.David;

import java.util.HashSet;
import java.util.Set;

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
}
