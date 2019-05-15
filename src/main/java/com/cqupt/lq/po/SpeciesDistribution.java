package com.cqupt.lq.po;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpeciesDistribution {

	private String term;
	private String organism;
	private static Map<String, Integer> dataMap;
	private  static List<String> filedList = new ArrayList<>();

	static {
		filedList.add("Organism");
		filedList.add("Term");
	}

	public SpeciesDistribution() {
		dataMap = new HashMap<>(16);
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public static Map<String, Integer> getDataMap() {
		return dataMap;
	}

	public static void setDataMap(Map<String, Integer> dataMap) {
		SpeciesDistribution.dataMap = dataMap;
	}

	public static List<String> getFiledList() {
		return filedList;
	}

	public static void setFiledList(List<String> filedList) {
		SpeciesDistribution.filedList = filedList;
	}
}
