package com.cqupt.lq.po;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class David implements Comparable<David> {
	private String entry;
	private String url;
	private Integer Hosts;
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

	public Integer getHosts() {
		return Hosts;
	}

	public void setHosts(Integer hosts) {
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
	public String toString() {
		return "David{" +
				"entry='" + entry + '\'' +
				", url='" + url + '\'' +
				", Hosts=" + Hosts +
				", Label='" + Label + '\'' +
				", biocarta='" + biocarta + '\'' +
				", keggPathway='" + keggPathway + '\'' +
				'}';
	}

	@Override
	public int compareTo(David o) {
		return o.getHosts().compareTo(this.getHosts());
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		David david = (David) o;
		return Objects.equals(entry, david.entry);
	}

	@Override
	public int hashCode() {

		return Objects.hash(entry);
	}
}
