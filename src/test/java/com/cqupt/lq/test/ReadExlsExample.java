package com.cqupt.lq.test;

import com.cqupt.lq.manager.NrdManager;
import com.cqupt.lq.po.Nrd;
import com.cqupt.lq.po.Reactome;
import com.cqupt.lq.manager.ReactomeManager;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import uk.ac.ebi.kraken.interfaces.uniprot.*;
import uk.ac.ebi.kraken.interfaces.uniprot.comments.Comment;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Field;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Flag;
import uk.ac.ebi.kraken.interfaces.uniprot.description.Name;
import uk.ac.ebi.kraken.interfaces.uniprot.evidences.EvidenceAttribute;
import uk.ac.ebi.kraken.interfaces.uniprot.evidences.EvidenceId;
import uk.ac.ebi.kraken.interfaces.uniprot.evidences.EvidenceType;
import uk.ac.ebi.kraken.interfaces.uniprot.genename.GeneNameSynonym;
import uk.ac.ebi.kraken.interfaces.uniprot.genename.ORFName;
import uk.ac.ebi.kraken.interfaces.uniprot.genename.OrderedLocusName;
import uk.ac.ebi.kraken.interfaces.uniprot.organism.OrganismName;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.QueryResult;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtComponent;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtQueryBuilder;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;
import uk.ac.ebi.uniprot.dataservice.query.Query;

import java.io.*;

public class ReadExlsExample {

	@Test
	public void mthod1() {
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream("H:\\IDEASpace\\Reactome\\src\\main\\resources\\targets_without_bots.csv")));
			String line = "";
			while (!"".equals(line = bufferedReader.readLine()) && line != null) {
				Reactome reactome = new Reactome();
				String[] s = line.split(",");
				reactome.setTerm(s[0]);
				reactome.setUniqueUsers(s[1]);
				reactome.setHits(s[2]);
				ReactomeManager.getReactomeManager().getReactomeList().add(reactome);
			}
			System.out.println(ReactomeManager.getReactomeManager().getReactomeList());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.fillInStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void method2() throws ServiceException {
		ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();      // get protein data ServiceFactory
		UniProtService uniprotService = serviceFactoryInstance.getUniProtQueryService();// get UniprotService

		uniprotService.start();
//		Query query = UniProtQueryBuilder.gene("TNFSF5IP1");           // create a query
//		Query query = UniProtQueryBuilder.keyword("TNFSF5IP1");//不行
//		Query query = UniProtQueryBuilder.proteinName("TNFSF5IP1");
//		Query query = UniProtQueryBuilder.gene("Q9Y3A5");

		Query query = UniProtQueryBuilder.xref("O60225");

//		System.out.println("-----------------------------genes-----------------------------");
//		result(query, uniprotService);
//		System.out.println("-----------------------------KeyWords-----------------------------");
//		resultKeyWords(query, uniprotService);
//		System.out.println("-----------------------------ProteinNames-----------------------------");
//		resultProteinNames(query, uniprotService);
		System.out.println("-----------------------------resultEntries-----------------------------");
		resultEntries(query, uniprotService);
		uniprotService.stop();
	}

	public void result(Query query, UniProtService uniprotService) throws ServiceException {
		// Specify that we want returned just the genes, using explicit getGenes method
		QueryResult<UniProtComponent<Gene>> geneComponents = uniprotService.getGenes(query);
		while (geneComponents.hasNext()) {
			UniProtComponent<Gene> geneComponent = geneComponents.next();
			if (!geneComponent.getComponent().isEmpty()) {
				System.out.println("accession " + geneComponent.getAccession().getValue());
				for (Gene gene : geneComponent.getComponent()) {
					System.out.print("gene ");
					System.out.println(gene.getGeneName());
					for (GeneNameSynonym geneNameSynonym : gene.getGeneNameSynonyms()) {
						for (EvidenceId evidenceId : geneNameSynonym.getEvidenceIds()) {
							System.out.print("evidenceId ");
							System.out.println(evidenceId.getTypeValue()+" --- "+evidenceId.getValue());
							EvidenceAttribute attribute = evidenceId.getAttribute();
							System.out.print("attribute ");
							System.out.println(attribute.getValue());
							EvidenceType type = evidenceId.getType();
							System.out.print("evidenceType ");
							System.out.println(type.getValue());
							System.out.println("================================================");
						}
					}
					for (OrderedLocusName orderedLocusName : gene.getOrderedLocusNames()) {
						System.out.print("orderedLocusName :");
						System.out.println(orderedLocusName.getValue() +" --- "+orderedLocusName.getEvidenceIds());
						System.out.println("================================================");
					}
					for (ORFName orfName : gene.getORFNames()) {
						System.out.print("orfName :");
						System.out.println(orfName.getValue());
						System.out.println("================================================");
					}
				}
			}
		}
	}

	public void resultKeyWords(Query query, UniProtService uniprotService) throws ServiceException {
		QueryResult<UniProtComponent<Keyword>> keywords = uniprotService.getKeywords(query);
		while (keywords.hasNext()) {
			UniProtComponent<Keyword> next = keywords.next();
			if (!next.getComponent().isEmpty()) {
				System.out.println("accession " + next.getAccession().getValue());
				for (Keyword keyword : next.getComponent()) {
					System.out.print("keyword ");
					System.out.println(keyword.getValue());
				}
			}
		}
	}

	public void resultProteinNames(Query query, UniProtService uniprotService) throws ServiceException {
		QueryResult<UniProtComponent<String>> proteinNames = uniprotService.getProteinNames(query);
		while (proteinNames.hasNext()) {
			UniProtComponent<String> next = proteinNames.next();
			if (!next.getComponent().isEmpty()) {
				System.out.println("accession " + next.getAccession().getValue());
				System.out.println("protein"+next.getComponent());
			}
		}
	}

	public void resultEntries(Query query, UniProtService uniprotService) throws ServiceException {
		QueryResult<UniProtEntry> entries = uniprotService.getEntries(query);
		while (entries.hasNext()) {
			UniProtEntry next = entries.next();
			System.out.println("==============================PrimaryUniProtAccession================================");
			System.out.println(next.getPrimaryUniProtAccession().getValue());
			System.out.println("================================ScientificName==============================");
			System.out.println(next.getOrganism().getScientificName().getValue());
			System.out.println("=================================CommonName==============================");
			System.out.println(next.getOrganism().getCommonName().getValue());
			System.out.println("=============================organismName=============================");
			for (OrganismName organismName : next.getOrganism().getOrganismNames()) {
				System.out.print(organismName.getValue()+"  ");
			}
			System.out.println();

			System.out.println("=============================gene=============================");
			for (Gene gene : next.getGenes()) {
				System.out.print(gene.getGeneName().getValue()+"  ");
			}

			System.out.println();

			System.out.println("=============================ProteinDescription=============================");
			for (Name name : next.getProteinDescription().getSection().getNames()) {
				for (Field field : name.getFields()) {
					System.out.println(field.getValue());
				}
			}

			System.out.println("=============================UniProtId=============================");
			System.out.println(next.getUniProtId().getValue());


			System.out.println();

			System.out.println("=============================EntryAudit=============================");
			System.out.println(next.getEntryAudit().getFirstPublicDate()+", "+next.getEntryAudit().getEntryVersion()+", "+next.getEntryAudit().getSequenceVersion());

			System.out.println("==============================SequenceLength================================");
			System.out.println(next.getSequence().getLength());
		}
	}

	public void resultEntry(String entry, UniProtService uniprotService) throws ServiceException {
		UniProtEntry entry1 = uniprotService.getEntry(entry);
		System.out.println("==============================organelle================================");
		for (Organelle organelle : entry1.getOrganelles()) {
			System.out.println(organelle.getValue()+",  ");
		}
		System.out.println("==============================ncbiTaxon================================");
		for (NcbiTaxon ncbiTaxon : entry1.getTaxonomy()) {
			if (ncbiTaxon != null) {
				System.out.println(ncbiTaxon.getValue() + "   ");
			}
		}
		System.out.println("==============================EntryAudit================================");
		System.out.println(entry1.getEntryAudit().toString());
		System.out.println("==============================Keyword================================");
		for (Keyword keyword : entry1.getKeywords()) {
			System.out.println(keyword.getValue()+",  ");
		}
		System.out.println("==============================comment================================");
		for (Comment comment : entry1.getComments()) {
			System.out.println(comment.getCommentType().toDisplayName() + ", " + comment.getCommentType().toXmlDisplayName());
		}
		System.out.println("==============================UniProtId================================");
		System.out.println(entry1.getUniProtId().getValue());
		System.out.println("==============================RecommendedName================================");
		System.out.println(entry1.getProteinDescription().getRecommendedName().toString());
		System.out.println("==============================AlternativeNames================================");
		for (Name name : entry1.getProteinDescription().getAlternativeNames()) {
			System.out.println(name.getFields()+", "+name.getNameType().getValue());
		}
		System.out.println("==============================FlagType================================");
		for (Flag flag : entry1.getProteinDescription().getFlags()) {
			System.out.println(flag.getFlagType().getValue());
		}
		System.out.println("==============================Section================================");
		for (Name name : entry1.getProteinDescription().getSection().getNames()) {
			System.out.println(name.getNameType().getValue());
		}
		System.out.println("==============================EcNumbers================================");
		for (String s : entry1.getProteinDescription().getEcNumbers()) {
			System.out.println(s);
		}
		System.out.println("==============================SubNames================================");
		for (Name name : entry1.getProteinDescription().getSubNames()) {
			System.out.println(name.getNameType().getValue());
		}
		System.out.println("==============================Genes================================");
		for (Gene gene : entry1.getGenes()) {
			System.out.println(gene.getGeneName().toString());
		}
		System.out.println("==============================Organism================================");
		for (OrganismName organismName : entry1.getOrganism().getOrganismNames()) {
			System.out.println(organismName.getValue());
		}
		System.out.println("Synonym:  "+entry1.getOrganism().getSynonym());
		System.out.println("CommonName:  "+entry1.getOrganism().getCommonName().getValue());
		System.out.println("ScientificName:  "+entry1.getOrganism().getScientificName().toString());
		System.out.println("==============================PrimaryUniProtAccession================================");
		System.out.println(entry1.getPrimaryUniProtAccession().getValue());
		System.out.println("==============================SequenceLength================================");
		System.out.println(entry1.getSequence().getLength());
	}


	@Test
	public void method(){


	}
}
