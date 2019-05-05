package com.cqupt.lq.test;

import com.cqupt.lq.util.HttpClient;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.message.BasicHeader;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetInfo {
	@Test
	public void method(){
		try {
			HttpClient httpClient = new HttpClient("https://www.uniprot.org/uniprot/?query=TNFSF5IP1&sort=score");
//			Map<String, String> parameter = new HashMap<String ,String>();
//			parameter.put("from", "ACC+ID");
//			parameter.put("to", "ACC");
//			parameter.put("format", "tab");
//			parameter.put("query", "PI15");
//			parameter.put("sort", "score");
//			httpClient.setParameter(parameter);
			httpClient.setHttps(true);
			httpClient.setHeader(new BasicHeader("User-Agent", "Python s180501002@stu.cqupt.edu.cn"));
			httpClient.get();
			String content = httpClient.getContent();
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void setHyperlinks() throws Exception {
		Workbook wb = new XSSFWorkbook(); //or new HSSFWorkbook();
		CreationHelper createHelper = wb.getCreationHelper();

		//cell style for hyperlinks
		//by default hyperlinks are blue and underlined
		CellStyle hlink_style = wb.createCellStyle();
		Font hlink_font = wb.createFont();
		hlink_font.setUnderline(Font.U_SINGLE);
		hlink_font.setColor(IndexedColors.BLUE.getIndex());
		hlink_style.setFont(hlink_font);

		Cell cell;
		Sheet sheet = wb.createSheet("Hyperlinks");
		//URL
		cell = sheet.createRow(0).createCell(0);
		cell.setCellValue("URL Link");

		Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
		link.setAddress("http://poi.apache.org/");
		cell.setHyperlink(link);
		cell.setCellStyle(hlink_style);

		//create a target sheet and cell
		Sheet sheet2 = wb.createSheet("Target Sheet");
		sheet2.createRow(0).createCell(0).setCellValue("Target Cell");

		cell = sheet.createRow(3).createCell(0);
		cell.setCellValue("Worksheet Link");
		Hyperlink link2 = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		link2.setAddress("'Target Sheet'!A1");
		cell.setHyperlink(link2);
		cell.setCellStyle(hlink_style);

		try (OutputStream out = new FileOutputStream("Hyperlink.xlsx")) {
			wb.write(out);
		}

		wb.close();
	}

}

