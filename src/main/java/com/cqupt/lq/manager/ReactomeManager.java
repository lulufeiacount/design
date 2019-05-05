package com.cqupt.lq.manager;

import com.cqupt.lq.po.Reactome;

import java.util.ArrayList;
import java.util.List;

public class ReactomeManager{

	private static ReactomeManager reactomeManager = new ReactomeManager();
	private List<Reactome> reactomeList;

	public static ReactomeManager getReactomeManager(){
		return reactomeManager;
	}

	public List<Reactome> getReactomeList() {
		return reactomeList;
	}

	private ReactomeManager(){
		reactomeList = new ArrayList<Reactome>();
	}
}
