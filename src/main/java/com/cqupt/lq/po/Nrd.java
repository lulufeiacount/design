package com.cqupt.lq.po;

public class Nrd {
	private String label;
	private String entry;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	public Nrd(String label, String entry) {
		this.label = label;
		this.entry = entry;
	}

	public Nrd() {
	}

	@Override
	public String toString() {
		return "Nrd{" +
				"label='" + label + '\'' +
				", entry='" + entry + '\'' +
				'}';
	}
}
