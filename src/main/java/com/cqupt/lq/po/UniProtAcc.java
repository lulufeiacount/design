package com.cqupt.lq.po;

import java.util.ArrayList;
import java.util.List;

public class UniProtAcc implements Comparable<UniProtAcc>{

	private String Term;
	private String Entry;
	private String Organism;
	public static List<String> filedList = new ArrayList<>();

	static {
		filedList.add("Term");
		filedList.add("Entry");
		filedList.add("Organism");
	}

	public String getTerm() {
		return Term;
	}

	public void setTerm(String term) {
		Term = term;
	}

	public String getEntry() {
		return Entry;
	}

	public void setEntry(String entry) {
		Entry = entry;
	}

	public String getOrganism() {
		return Organism;
	}

	public void setOrganism(String organism) {
		Organism = organism;
	}

	public UniProtAcc(String term) {
		Term = term;
	}

	public UniProtAcc() {
	}

	public UniProtAcc(String term, String entry, String organism) {
		Term = term;
		Entry = entry;
		Organism = organism;
	}

	@Override
	public String toString() {
		return "UniProtAcc{" +
				"Term='" + Term + '\'' +
				", Entry='" + Entry + '\'' +
				", Organism='" + Organism + '\'' +
				'}';
	}

	@Override
	public int compareTo(UniProtAcc o) {
		return this.getOrganism().compareTo(o.Organism);
	}
}
