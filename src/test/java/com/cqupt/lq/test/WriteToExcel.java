package com.cqupt.lq.test;

import com.cqupt.lq.po.UniProt;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class WriteToExcel {

	private static final String EXCEL_XLSX = "xlsx";
	private static final String EXCEL_XLS = "xls";

	public static void main(String[] args) throws Exception {
		List<String> titleList = new ArrayList<>();
		titleList.add("Term");
		titleList.add("Entry");
		titleList.add("Organism");
		UniProt uniProt = new UniProt();
		uniProt.setTerm("119");
		uniProt.setEntry("1234");
		uniProt.setOrganism("human");

		List<UniProt> dataList=new ArrayList<>();
		dataList.add(uniProt);
		writeExcel("啦啦啦", titleList, dataList, "C:/Users/Cockroach/Desktop/writeExcel.xlsx");
	}

	/**
	 * @param tileList 标题内容
	 * @param dataList 数据内容
	 * @param finalXlsxPath 输出文件路径
	 */
	public static void writeExcel(String sheetName, List<String> tileList, List<UniProt> dataList, String finalXlsxPath){
		OutputStream out = null;
		try {
			// 读取Excel文档
			File finalXlsxFile = new File(finalXlsxPath);
			if (!finalXlsxFile.exists()){
				finalXlsxFile.createNewFile();
			}
			Workbook workBook = getWorkbok(finalXlsxFile);
			//设置标题样式
			CellStyle cellStyle = workBook.createCellStyle();
			//设置背景颜色为蓝色
			cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);// 设置前景色
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			//设置字体格式
			Font font = workBook.createFont();
			font.setBold(true);
			cellStyle.setFont(font);
			// sheet 对应一个工作页
			Sheet sheet = workBook.createSheet(sheetName);
			//将第一列冻结
			sheet.createFreezePane( 0, 1, 0, 1 );

			//标题
			Row titleRow = sheet.createRow(0);
			for (int i = 0; i < tileList.size(); i++) {
				Cell cell = titleRow.createCell(i);
				cell.setCellValue(tileList.get(i));
				cell.setCellStyle(cellStyle);
			}
			//往Excel中写新数据
			for (int j = 0; j < dataList.size(); j++) {
				// 创建一行：从第二行开始，跳过属性列
				Row row = sheet.createRow(j + 1);
				// 得到要插入的每一条记录
				UniProt uniProt = dataList.get(j);

				Cell first = row.createCell(0);
				first.setCellValue(uniProt.getTerm());

				Cell second = row.createCell(1);
				second.setCellValue(uniProt.getEntry());

				Cell third = row.createCell(2);
				third.setCellValue(uniProt.getOrganism());
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
	 * 判断Excel的版本,获取Workbook
	 * @return
	 * @throws IOException
	 */
	public static Workbook getWorkbok(File file) throws IOException{
		Workbook wb = null;
		OutputStream fileOut = new FileOutputStream(file);
		if(file.getName().endsWith(EXCEL_XLS)){
			wb = new HSSFWorkbook();//Excel&nbsp;2003
		}else if(file.getName().endsWith(EXCEL_XLSX)){
			wb = new XSSFWorkbook();// Excel 2007/2010
		}
		wb.write(fileOut);
		return wb;
	}



}
