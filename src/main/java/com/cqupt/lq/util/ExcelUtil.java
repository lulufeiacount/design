package com.cqupt.lq.util;

import com.cqupt.lq.manager.NrdManager;
import com.cqupt.lq.po.*;
import com.cqupt.lq.manager.ReactomeManager;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class ExcelUtil {
	private static final String EXCEL_XLSX = "xlsx";
	private static final String EXCEL_XLS = "xls";
	private static CellStyle hlink_style;

	public static void main(String[] args) {
//		List<String> titleList = new ArrayList<>();
//		titleList.add("Organism");
//		titleList.add("Term");
//		Map<String, Integer> map = new HashMap<>();
//		map.put("human", 23);
//		map.put("mouse", 2);
//		map.put("rabbit", 2);
//		insertPicToExcelAndData("C:\\Users\\Cockroach\\Desktop\\pic.jpg", "C:\\Users\\Cockroach\\Desktop\\1.xlsx","Species Distribution",titleList,map);

		List<String> titleList = new ArrayList<>();
		titleList.add("Term");
		titleList.add("Entry");
		titleList.add("Organism");
		UniProtAcc uniProt = new UniProtAcc();
		uniProt.setTerm("119");
		uniProt.setEntry("1234");
		uniProt.setOrganism("human");

		List<UniProtAcc> dataList=new ArrayList<>();
		dataList.add(uniProt);
		writeExcel("啦啦啦", titleList, dataList, "C:/Users/Cockroach/Desktop/writeExcel.xlsx");

	}

	/**
	 * @param filePath csv文件位置。
	 */
	public static void readCSVToReactome(String filePath){
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
			String line;
			while (!"".equals(line = bufferedReader.readLine()) && line != null) {
				Reactome reactome = new Reactome();
				String[] s = line.split(",");
				reactome.setTerm(s[0]);
				reactome.setUniqueUsers(s[1]);
				reactome.setHits(s[2]);
				ReactomeManager.getReactomeManager().getReactomeList().add(reactome);
			}
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


	/**
	 * @param sheetName 表空间名字
	 * @param titleList 标题头内容
	 * @param dataList 数据内容
	 * @param finalXlsxPath 输出文件位置
	 */
	public static void writeExcel(String sheetName, List<String> titleList, List<UniProtAcc> dataList, String finalXlsxPath){
		OutputStream out = null;
		try {
			Workbook workBook = getWorkbok(finalXlsxPath);
			// sheet 对应一个工作页
			Sheet sheet = workBook.createSheet(sheetName);

			// 设置标题
			setExcelTile(workBook, sheet, titleList);

			//往Excel中写新数据
			for (int j = 0; j < dataList.size(); j++) {
				// 创建一行：从第二行开始，跳过属性列
				Row row = sheet.createRow(j + 1);
				// 得到要插入的每一条记录
				UniProtAcc uniProtAcc = dataList.get(j);

				Cell first = row.createCell(0);
				first.setCellValue(uniProtAcc.getTerm());

				Cell second = row.createCell(1);
				second.setCellValue(uniProtAcc.getEntry());

				Cell third = row.createCell(2);
				third.setCellValue(uniProtAcc.getOrganism());
			}
			// 创建文件输出流，准备输出电子表格：这个必须有，否则你在sheet上做的任何操作都不会有效
			out =  new FileOutputStream(finalXlsxPath);
			workBook.write(out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				if(out != null){
					out.flush();
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param picFilePath 图片文件路径
	 * @param excelFilePath excel文件路径
	 */
	public static void insertPicToExcelAndData(String picFilePath, String excelFilePath, String sheetName, List<String> titleList, Map<String, Integer> dataMap){
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		//先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			bufferImg = ImageIO.read(new File(picFilePath));
			ImageIO.write(bufferImg, "jpg", byteArrayOut);
			FileInputStream in = new FileInputStream(excelFilePath);
			XSSFWorkbook wb = new XSSFWorkbook(in);
			//将读一个表空间隐藏。
			wb.getSheetAt(0).setSelected(false);
			Sheet sheet = wb.createSheet(sheetName);
			sheet.setSelected(true);
			//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
			Drawing drawingPatriarch = sheet.createDrawingPatriarch();
			//anchor主要用于设置图片的属性
			XSSFClientAnchor anchor = new XSSFClientAnchor(0, 0, 0, 0, (short) 4, 1, (short) 24, 39);
			anchor.setAnchorType(3);
			//插入图片
			drawingPatriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), XSSFWorkbook.PICTURE_TYPE_JPEG));

			//设置标题
			setExcelTile(wb, sheet, titleList);
			Iterator<String> iterator = dataMap.keySet().iterator();
			//写入数据
			for (int i = 0; i < dataMap.keySet().size(); i++) {
				// 创建一行：从第二行开始，跳过属性列
				Row row = sheet.createRow(i + 1);
				String key = iterator.next();
				row.createCell(0).setCellValue(key);
				row.createCell(1).setCellValue(dataMap.get(key));
			}

			fileOut = new FileOutputStream(excelFilePath);
			// 写入excel文件
			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fileOut != null){
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	/**
	 * 将表格内容读取到
	 * @param filePath
	 */
	public static void readEecelToManager(String filePath){
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(filePath));
			XSSFSheet sheetAt = workbook.getSheetAt(0);
			for (int i = 1; i < sheetAt.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = sheetAt.getRow(i);
				NrdManager.getNrdManager().getNrdList().add(new Nrd(row.getCell(1).toString(), row.getCell(6).toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 第三个表空间内容
	 * @param excelFilePath 文件路径
	 * @param sheetName 表空间名字
	 * @param titleList 标题集合
	 * @param dataList 内容集合
	 */
	public static void writeSheet3ToExcel(String excelFilePath, String sheetName, List<String> titleList, Set<David> dataList) {
		FileOutputStream fileOut = null;
		try {
			FileInputStream in = new FileInputStream(excelFilePath);
			XSSFWorkbook wb = new XSSFWorkbook(in);
			//将读一个表空间隐藏。
			wb.getSheetAt(0).setSelected(false);
			wb.getSheetAt(1).setSelected(false);
			Sheet sheet = wb.createSheet(sheetName);
			sheet.setSelected(true);

			//设置标题
			setExcelTile(wb, sheet, titleList);
			Iterator<David> iterator = dataList.iterator();
			int i = 1;
			//写入数据
			while (iterator.hasNext()) {
				// 创建一行：从第二行开始，跳过属性列
				Row row = sheet.createRow(i);
				David david = iterator.next();
				setHyperlinks(wb,row,david.getEntry(),david.getUrl(),0);

				Cell first = row.createCell(1);
				first.setCellValue(david.getHosts());

				Cell second = row.createCell(2);
				second.setCellValue(david.getLabel());

				if (david.getBiocarta() != null) {
					Cell third = row.createCell(3);
					third.setCellValue(david.getBiocarta());

					Cell forth = row.createCell(4);
					forth.setCellValue(david.getKeggPathway());
				}
				i++;
			}
			fileOut = new FileOutputStream(excelFilePath);
			// 写入excel文件
			wb.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fileOut != null){
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 设置标题
	 * @param workbook
	 * @param sheet
	 * @param titleList
	 */
	private static void setExcelTile(Workbook workbook, Sheet sheet, List<String> titleList) {
		//设置标题样式
		CellStyle cellStyle = workbook.createCellStyle();
		//设置背景颜色为蓝色
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);// 设置前景色
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		//设置字体格式
		Font font = workbook.createFont();
		font.setBold(true);
		cellStyle.setFont(font);
		//将第一列冻结
		sheet.createFreezePane( 0, 1, 0, 1 );
		//标题
		Row titleRow = sheet.createRow(0);
		for (int i = 0; i < titleList.size(); i++) {
			Cell cell = titleRow.createCell(i);
			cell.setCellValue(titleList.get(i));
			cell.setCellStyle(cellStyle);
		}
	}


	/**
	 * 创建超链接
	 * @param wb Excel工作对象
	 * @param row 行对象
	 * @param value 单元格值
	 * @param url 超链接地址
	 * @param cellNum 单元格列位置
	 */
	public static void setHyperlinks(Workbook wb, Row row, String value, String url, Integer cellNum) {
		CreationHelper createHelper = wb.getCreationHelper();

		if (hlink_style == null) {
			CellStyle hlink_style = wb.createCellStyle();
			Font hlink_font = wb.createFont();
			hlink_font.setUnderline(Font.U_SINGLE);
			hlink_font.setColor(IndexedColors.BLUE.getIndex());
			hlink_style.setFont(hlink_font);
		}

		Cell cell;
		//URL
		cell = row.createCell(cellNum);
		cell.setCellValue(value);

		Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
		link.setAddress(url);
		cell.setHyperlink(link);
		cell.setCellStyle(hlink_style);
	}



	/**
	 * 判断Excel的版本,获取Workbook
	 * @return
	 * @throws IOException
	 */
	private static Workbook getWorkbok(String file) throws IOException {
		Workbook wb = null;
		OutputStream fileOut = new FileOutputStream(file);
		if(file.endsWith(EXCEL_XLS)){
			wb = new HSSFWorkbook();//Excel&nbsp;2003
		}else if(file.endsWith(EXCEL_XLSX)){
			wb = new XSSFWorkbook();// Excel 2007/2010
		}
		wb.write(fileOut);
		return wb;
	}
}
