package com.cqupt.lq.test;

import david.xsd.AnnotationRecord;
import david.xsd.ArrayOfString;
import david.xsd.TableRecord;
import org.apache.axis2.AxisFault;
import org.junit.Test;
import sample.session.client.stub.DAVIDWebServiceStub;

import java.util.Hashtable;

public class DavidTest {
	private static int count = 0;

	private String inputIds = "A0A096LP55,P49335,Q96BY9";
	private String idType = "UNIPROT_ACCESSION";
	private String listName= "annotation result";
	private String categoryNames= "BIOCARTA,KEGG_PATHWAY";
	private int listType= 0;

	@Test
	public  void getResultFromDavid() {
		try {
			DAVIDWebServiceStub stub =
					new DAVIDWebServiceStub(
							"https://david.ncifcrf.gov/webservice/services/DAVIDWebService?wsdl");
			stub._getServiceClient().getOptions().setManageSession(true);
			System.out.println();
			String userVerification = stub.authenticate("903442459@qq.com");
//
			System.out.println("User authentication: " + userVerification);
			System.out.println();
			if (userVerification.equals("true")) {
				try {
					stub.addList(inputIds, idType, listName, listType);
					TableRecord[] tableRecords = stub.getTableReport();

					String validCategoryString = stub.setCategories(categoryNames);

					System.out.println("valid Category Strings: " + validCategoryString);
					String[] category_strings = validCategoryString.split(",");

					if (tableRecords.length > 0) {
						for (int j = 0; j < tableRecords.length; j++) {
							ArrayOfString[] values = tableRecords[j].getValues();

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
									System.out.println(terms_string);
								}
							}

						}
					}
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
