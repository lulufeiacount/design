package com.cqupt.lq.manager;

import com.cqupt.lq.po.Nrd;
import com.cqupt.lq.po.Reactome;

import java.util.ArrayList;
import java.util.List;

public class NrdManager{

	private static NrdManager nrdManager = new NrdManager();
	private List<Nrd> nrdList;

	public static NrdManager getNrdManager(){
		return nrdManager;
	}

	public List<Nrd> getNrdList() {
		return nrdList;
	}

	private NrdManager(){
		nrdList = new ArrayList<Nrd>();
	}
}
