package com.cqupt.lq.po;

public class Reactome {
	private String Term;
	private String uniqueUsers;
	private String hits;

	public String getTerm() {
		return Term;
	}

	public void setTerm(String term) {
		Term = term;
	}

	public String getUniqueUsers() {
		return uniqueUsers;
	}

	public void setUniqueUsers(String uniqueUsers) {
		this.uniqueUsers = uniqueUsers;
	}

	public String getHits() {
		return hits;
	}

	public void setHits(String hits) {
		this.hits = hits;
	}

	@Override
	public String toString() {
		return "Reactome{" +
				"Term='" + Term + '\'' +
				", uniqueUsers='" + uniqueUsers + '\'' +
				", hits='" + hits + '\'' +
				'}';
	}
}
