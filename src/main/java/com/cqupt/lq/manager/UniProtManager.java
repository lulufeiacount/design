package com.cqupt.lq.manager;

import com.cqupt.lq.po.Reactome;
import com.cqupt.lq.po.UniProt;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询结果集管理类
 */
public class UniProtManager {

	private static UniProtManager uniProtManager = new UniProtManager();
	private List<UniProt> uniProtList;

	public static UniProtManager getUniProtManager(){
		return uniProtManager;
	}

	public List<UniProt> getUniProtList() {
		return uniProtList;
	}

	private UniProtManager(){
		uniProtList = new ArrayList<>();
	}
}
