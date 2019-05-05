package com.cqupt.lq.manager;

import com.cqupt.lq.po.UniProtAcc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniProtAccManager {

	private static UniProtAccManager uniProtAccManager = new UniProtAccManager();
	private List<UniProtAcc> uniProtAccList;

	public static UniProtAccManager getUniProtAccManager(){
		return uniProtAccManager;
	}

	public List<UniProtAcc> getUniProtAccList() {
		Collections.sort(uniProtAccList);
		return uniProtAccList;
	}


	private UniProtAccManager(){
		uniProtAccList = new ArrayList<>();
	}
}
