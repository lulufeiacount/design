package com.cqupt.lq.service;

import com.cqupt.lq.manager.DavidManager;
import com.cqupt.lq.po.David;
import com.cqupt.lq.po.UniProt;
import david.xsd.AnnotationRecord;
import david.xsd.TableRecord;
import org.apache.axis2.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.session.client.stub.DAVIDWebServiceStub;

import java.util.Hashtable;
import java.util.List;

public class DavidService implements Service {

	private static final Logger logger = LoggerFactory.getLogger(DavidService.class);

	private static int count = 0;

	private List<UniProt> uniProtList;
	private String idType;
	private String listName;
	private String categoryNames;
	private int listType;


	public DavidService(List<UniProt> uniProtList) {
		this.uniProtList = uniProtList;
		this.idType = "UNIPROT_ACCESSION";
		this.listName = "annotation result";
		this.categoryNames = "BIOCARTA,KEGG_PATHWAY";
		this.listType = 0;
	}

	/**
	 * @return 0表示次数满足200 终止  1 表示正常终止。
	 */
	public int getResult(){
		if (uniProtList != null && uniProtList.size() != 0){
			for (int i = 0; i < uniProtList.size(); i++) {
				String[] entry = uniProtList.get(i).getEntry().split(",");
				logger.debug("开始向David发送第："+count+"个entry。");
				for (String s : entry) {
					if (count == 12000){
						return 0;
					}
					getResultFromDavid(new David(s));
					count++;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return 1;
	}

	private void getResultFromDavid(David david) {
		logger.debug("开始向David查询entry："+david.getEntry());
		try {
			DAVIDWebServiceStub stub =
					new DAVIDWebServiceStub(
							"https://david.ncifcrf.gov/webservice/services/DAVIDWebService?wsdl");
			stub._getServiceClient().getOptions().setManageSession(true);
			System.out.println();
			String userVerification = stub.authenticate("903442459@qq.com");

			System.out.println("User authentication: " + userVerification);
			System.out.println();
			if (userVerification.equals("true")) {
				try {
					stub.addList(david.getEntry(), idType, listName, listType);
					TableRecord[] tableRecords = stub.getTableReport();

					String validCategoryString = stub.setCategories(categoryNames);

					String[] category_strings = validCategoryString.split(",");

					if (tableRecords.length > 0) {
						for (int j = 0; j < tableRecords.length; j++) {

							AnnotationRecord[] annotationRecords = tableRecords[j].getAnnotationRecords();

							Hashtable<String, String> annotation_hash = new Hashtable<>();

							for (int k = 0; k < annotationRecords.length; k++) {
								String annotationRecordCategory = annotationRecords[k].getCategory();
								String terms_string = "";
								String[] annotationRecordTerms = annotationRecords[k].getTerms();

								for (int i = 0; i < annotationRecordTerms.length; i++) {
									String termString = getTermString(annotationRecordTerms[i]);
									terms_string = terms_string + termString + ",";
								}
								annotation_hash.put(annotationRecordCategory, terms_string);
							}
							for (int k = 0; k < category_strings.length; k++) {
								String terms_string = annotation_hash.get(category_strings[k]);
								if (terms_string != null) {
									david.setBiocarta(terms_string);
									david.setKeggPathway(terms_string);
								}
							}

						}
					}
					DavidManager.getDavidManager().getDavidSet().add(david);
				} catch (java.io.IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					System.out.println("Unknown exception:" + e.toString());
				}
			}
		} catch (AxisFault axisFault) {
			axisFault.printStackTrace();
		} catch (java.rmi.RemoteException e) {
			e.printStackTrace();
		}
	}

	private String getTermString(String var1) {
		String[] var2 = null;
		String var3 = "\\$";
		var2 = var1.split(var3);
		return var2 == null ? var1 : var2[var2.length - 1];
	}
}
