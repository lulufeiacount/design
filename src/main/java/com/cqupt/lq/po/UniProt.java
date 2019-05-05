package com.cqupt.lq.po;

import java.util.List;

public class UniProt {

	private String term;
	private String uniqueUsers;
	private String entry;
	private String entryName;
	private String status;
	private List<String> proteinNameList;
	private List<String> geneNamesList;
	private String organism;
	private String sequenceLength;

	public String getUniqueUsers() {
		return uniqueUsers;
	}

	public void setUniqueUsers(String uniqueUsers) {
		this.uniqueUsers = uniqueUsers;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public String getEntryName() {
		return entryName;
	}

	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getProteinNameList() {
		return proteinNameList;
	}

	public void setProteinNameList(List<String> proteinNameList) {
		this.proteinNameList = proteinNameList;
	}

	public List<String> getGeneNamesList() {
		return geneNamesList;
	}

	public void setGeneNamesList(List<String> geneNamesList) {
		this.geneNamesList = geneNamesList;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public String getSequenceLength() {
		return sequenceLength;
	}

	public void setSequenceLength(String sequenceLength) {
		this.sequenceLength = sequenceLength;
	}

	@Override
	public String toString() {
		return "UniProt{" +
				"term='" + term + '\'' +
				", entry='" + entry + '\'' +
				", entryName='" + entryName + '\'' +
				", status='" + status + '\'' +
				", proteinNameList=" + proteinNameList +
				", geneNamesList=" + geneNamesList +
				", organism='" + organism + '\'' +
				", sequenceLength='" + sequenceLength + '\'' +
				'}';
	}

	public UniProt(String term) {
		this.term = term;
	}

	public UniProt(String term, String uniqueUsers) {
		this.term = term;
		this.uniqueUsers = uniqueUsers;
	}

	public UniProt() {
	}
}
