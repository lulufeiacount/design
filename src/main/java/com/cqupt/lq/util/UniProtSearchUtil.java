package com.cqupt.lq.util;

import com.cqupt.lq.po.Reactome;
import com.cqupt.lq.po.UniProt;
import uk.ac.ebi.kraken.interfaces.uniprot.Gene;
import uk.ac.ebi.kraken.interfaces.uniprot.UniProtEntry;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.QueryResult;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtQueryBuilder;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;
import uk.ac.ebi.uniprot.dataservice.query.Query;

import java.util.ArrayList;
import java.util.List;

public class UniProtSearchUtil {

	public static void main(String[] args) {
		ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();      // get protein data ServiceFactory
		UniProtService uniprotService = serviceFactoryInstance.getUniProtQueryService();
		Reactome reactome = new Reactome();
		reactome.setTerm("znf91");
		uniprotService.start();
		try {
			for (UniProt uniProt : searchInfoEntries(reactome, uniprotService)) {
				System.out.println(uniProt);
			}
		} catch (ServiceException e) {
			e.printStackTrace();
		}finally {
			uniprotService.stop();
		}
	}

	public static List<UniProt> searchInfoEntries(Reactome reactome, UniProtService uniProtService) throws ServiceException {
		List<UniProt> uniProtList = new ArrayList<>();
		String term = reactome.getTerm();
		Query xref = UniProtQueryBuilder.xref(term);
		Query query = UniProtQueryBuilder.reviewed(xref, true);
		QueryResult<UniProtEntry> entries = uniProtService.getEntries(query);
		while (entries.hasNext()) {
			UniProt uniProt = new UniProt(term,reactome.getUniqueUsers());
			UniProtEntry next = entries.next();
//			System.out.println("==============================PrimaryUniProtAccession================================");
//			System.out.println(next.getPrimaryUniProtAccession().getValue());
			uniProt.setEntry(next.getPrimaryUniProtAccession().getValue());
			uniProt.setEntryName(next.getUniProtId().getValue());
//			System.out.println("================================ScientificName==============================");
//			System.out.println(next.getOrganism().getScientificName().getValue());
			uniProt.setOrganism(next.getOrganism().getScientificName().getValue()+" ("+next.getOrganism().getCommonName().getValue()+")");
//			System.out.println("=================================CommonName==============================");
//			System.out.println(next.getOrganism().getCommonName().getValue());
//			System.out.println("=============================organismName=============================");
//			for (OrganismName organismName : next.getOrganism().getOrganismNames()) {
//				System.out.print(organismName.getValue()+"  ");
//			}
//			System.out.println();
			List<String> geneNameList = new ArrayList<>();
//			System.out.println("=============================gene=============================");
			for (Gene gene : next.getGenes()) {
//				System.out.print(gene.getGeneName().getValue()+"  ");
				geneNameList.add(gene.getGeneName().getValue());
			}
			uniProt.setGeneNamesList(geneNameList);
//			System.out.println();


			List<String> proteinNameList = new ArrayList<>();
//			System.out.println("=============================ProteinDescription=============================");
			for (Name name : next.getProteinDescription().getSection().getNames()) {
				for (Field field : name.getFields()) {
//					System.out.println(field.getValue());
					proteinNameList.add(field.getValue());
				}
			}
			uniProt.setProteinNameList(proteinNameList);

//			System.out.println("=============================UniProtId=============================");
//			System.out.println(next.getUniProtId().getValue());


//			System.out.println();

//			System.out.println("=============================EntryAudit=============================");
//			System.out.println(next.getEntryAudit().getFirstPublicDate()+", "+next.getEntryAudit().getEntryVersion()+", "+next.getEntryAudit().getSequenceVersion());
			uniProt.setStatus("Reviewed");
//			System.out.println("==============================SequenceLength================================");
//			System.out.println(next.getSequence().getLength());
			uniProt.setSequenceLength(next.getSequence().getLength()+"");
			uniProtList.add(uniProt);
		}
		return uniProtList;
	}
}
