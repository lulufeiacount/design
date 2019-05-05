package com.cqupt.lq.util;

import com.cqupt.lq.po.SpeciesDistribution;
import com.cqupt.lq.po.UniProtAcc;
import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawingUtil {

	public static void main(String[] args) {
		UniProtAcc[] uniProtAccs = {new UniProtAcc("123", "Danio" ,"Danio rerio (Zebrafish) (Brachydanio rerio)"),
				 					new UniProtAcc("1231", "sanfo" ,"Homo sapiens (Human)"),
									new UniProtAcc("1232", "sanfodae" ,"Homo sapiens (Human)"),
									new UniProtAcc("12334", "koko" ,"Mus musculus (Mouse)"),
									new UniProtAcc("1233345", "coco" ,"Non-unique")};
		drawingPic(Arrays.asList(uniProtAccs), "C:\\Users\\Cockroach\\Desktop\\pic.jpg");
	}

	private static String[] colorArr = {"#749f83", "#2f4554", "#61a0a8", "#d48265", "#91c7ae", "#c23531", "#ca8622", "#bda29a", "#6e7074", "#546570", "#c4ccd3"};
	private static int totalSize;

	public static boolean drawingPic(List<UniProtAcc> uniProtAccList, String filePath) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		Map<String ,Integer> resultMap = parsingData(uniProtAccList);
		//将结果填充至对应类中
		SpeciesDistribution.setDataMap(resultMap);
		for (String key : resultMap.keySet()) {
			double result = (double)resultMap.get(key) * 100 / totalSize;
			dataset.setValue(key+"\n"+result+"%", new Double(resultMap.get(key)));
		}
		JFreeChart chart = ChartFactory.createPieChart("The Species Distribution", // chart
				dataset, // data
				true, // include legend
				true, false);
		setChart(chart);
		PiePlot pieplot = (PiePlot) chart.getPlot();
		int i = 0;
		for (String key : resultMap.keySet()) {
			pieplot.setSectionPaint(key, Color.decode(colorArr[i]));
			i++;
		}
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.createNewFile();
			}
			// 保存图片到指定文件夹
//			ChartUtilities.saveChartAsPNG(file, chart, 1500, 800);
			ChartUtilities.writeChartAsJPEG(new FileOutputStream(file), chart, 1500, 800);
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	private static void setChart(JFreeChart chart) {
		chart.setTextAntiAlias(true);

		PiePlot pieplot = (PiePlot) chart.getPlot();
		// 设置图表背景颜色
		pieplot.setBackgroundPaint(ChartColor.WHITE);


		pieplot.setLabelBackgroundPaint(null);// 标签背景颜色


		pieplot.setLabelOutlinePaint(null);// 标签边框颜色


		pieplot.setLabelShadowPaint(null);// 标签阴影颜色

		pieplot.setLabelFont(new Font("宋体",Font.BOLD,20));// 设置字体大小

		pieplot.setOutlinePaint(Color.decode("#c4ccd3")); // 设置绘图面板外边的填充颜色


		pieplot.setShadowPaint(Color.decode("#c4ccd3")); // 设置绘图面板阴影的填充颜色

		pieplot.setSectionOutlinesVisible(false);

		pieplot.setNoDataMessage("没有可供使用的数据！");

	}

	private static Map<String, Integer> parsingData(List<UniProtAcc> uniProtAccList) {
		Map<String, Integer> resultMap = new HashMap<>(16);
		for (UniProtAcc uniProtAcc : uniProtAccList) {
			if (resultMap.containsKey(uniProtAcc.getOrganism())) {
				resultMap.put(uniProtAcc.getOrganism(),resultMap.get(uniProtAcc.getOrganism())+1);
			} else {
				resultMap.put(uniProtAcc.getOrganism(), 1);
			}
			totalSize++;
		}
		return resultMap;
	}

}
