package com.cqupt.lq.test;

import com.cqupt.lq.util.HttpClient;
import org.junit.Test;

import java.io.IOException;

public class Http {

	@Test
	public void method() throws Exception {
		HttpClient httpClient = new HttpClient("http://david.abcc.ncifcrf.gov/api.jsp?type=ENTREZ_GENE_ID&ids=6347&tool=term2term&annot=" +
				"GOTERM_BP_FAT,GOTERM_CC_FAT,GOTERM_MF_FAT,INTERPRO,PIR_SUPERFAMILY,SMART,BBID," +
				"BIOCARTA,KEGG_PATHWAY,COG_ONTOLOGY,SP_PIR_KEYWORDS,UP_SEQ_FEATURE,GENETIC_ASSOCIATION_DB_DISEASE," +
				"OMIM_DISEASE");
		httpClient.get();
		System.out.println(httpClient.getContent());
	}
}
