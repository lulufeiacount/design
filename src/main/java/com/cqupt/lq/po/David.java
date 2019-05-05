package com.cqupt.lq.po;

import java.util.ArrayList;
import java.util.List;

public class David implements Comparable<David>{
	private String entry;
	private String url;
	private String Hosts;
	private String Label;
	private String biocarta;
	private String keggPathway;

	private static List<String> filedList = new ArrayList<>();
	static {
		filedList.add("Uniprot Accession Number");
		filedList.add("Hosts");
		filedList.add("Label");
		filedList.add("BIOCARTA");
		filedList.add("KEGG_PATHWAY");
	}

	public static List<String> getFiledList(){
		return filedList;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHosts() {
		return Hosts;
	}

	public void setHosts(String hosts) {
		Hosts = hosts;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getBiocarta() {
		return biocarta;
	}

	public void setBiocarta(String biocarta) {
		this.biocarta = biocarta;
	}

	public String getKeggPathway() {
		return keggPathway;
	}

	public void setKeggPathway(String keggPathway) {
		this.keggPathway = keggPathway;
	}

	public David(String entry) {
		this.entry = entry;
		this.url = "https://www.uniprot.org/uniprot/"+entry;
	}

	public David() {
	}

	@Override
	public int compareTo(David o) {
		return this.entry.compareTo(o.getEntry());
	}
}
