package com.cqupt.lq.controller;

import com.cqupt.lq.manager.*;
import com.cqupt.lq.po.*;
import com.cqupt.lq.service.DavidService;
import com.cqupt.lq.util.DrawingUtil;
import com.cqupt.lq.util.ExcelUtil;
import com.cqupt.lq.util.UniProtSearchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.uniprot.dataservice.client.Client;
import uk.ac.ebi.uniprot.dataservice.client.ServiceFactory;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;
import uk.ac.ebi.uniprot.dataservice.client.uniprot.UniProtService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ReactImpc {

	private static final Logger logger = LoggerFactory.getLogger(ReactImpc.class);

	private static String ORIGIN_FILE_PATH;
	private static String AIM_FILE_PATH;
	private static String PIC_FILE_PATH;
	private static String ORIGIN_NRD_FILE_PATH;
	private static UniProtService uniprotService;
	private volatile static boolean isLast = false;
	private volatile static boolean isWrite = false;
	private static ScheduledExecutorService scheduledExecutor;
	private volatile static int i = 1;

	static {
		ServiceFactory serviceFactoryInstance = Client.getServiceFactoryInstance();
		uniprotService = serviceFactoryInstance.getUniProtQueryService();
		scheduledExecutor = new ScheduledThreadPoolExecutor(2);
	}


	public static void  main(String[] args){
		if (args != null && args.length >= 2) {
			getStart(args[0], args[1], args[2]);
		}
		getStart("H:\\IDEASpace\\Reactome\\src\\main\\resources\\targets_without_bots(orginal).csv","C:\\Users\\Cockroach\\Documents\\Tencent Files\\903442459\\FileRecv\\reactomeannotationdashboard\\ReactomeAnnotationDashboard\\data\\nrd.2018.14-s3.xlsx", "C:\\Users\\Cockroach\\Desktop\\report_lite1.xlsx");
//		getStart("C:\\Users\\Cockroach\\Desktop\\reactome_year_report_20180418.txt","C:\\Users\\Cockroach\\Documents\\Tencent Files\\903442459\\FileRecv\\reactomeannotationdashboard\\ReactomeAnnotationDashboard\\data\\nrd.2018.14-s3.xlsx", "C:\\Users\\Cockroach\\Desktop\\report_lite.xlsx");
//		getStart("/home/stu/liqiang/reactome_year_report_20180418.txt","/home/stu/liqiang/nrd.2018.14-s3.xlsx", "/home/stu/liqiang/report_lite.xlsx");
	}

	/**
	 *
	 * @param originFilePath 原始文件路径
	 * @param originNrdFilePath Nrd模板路径
	 * @param aimFilePath 输出文件路径
	 */
	public static void getStart(String originFilePath,String originNrdFilePath, String aimFilePath){
		isWrite = false;
		if (originFilePath != null && originNrdFilePath != null && aimFilePath != null) {
			ORIGIN_FILE_PATH = originFilePath;
			AIM_FILE_PATH = aimFilePath;
			PIC_FILE_PATH = aimFilePath.replace("xlsx", "jpg");
			ORIGIN_NRD_FILE_PATH = originNrdFilePath;
			init();
			scheduledExecutor.scheduleWithFixedDelay(new SendInfoToUniProt(), 1, 1, TimeUnit.SECONDS);
			scheduledExecutor.scheduleWithFixedDelay(new CheckIsFileLast(), 10, 2, TimeUnit.SECONDS);
			while (true) {
				if (isLast && !isWrite) {
					//关闭服务
					uniprotService.stop();
					scheduledExecutor.shutdown();
					//请求David服务
					new DavidService(UniProtManager.getUniProtManager().getUniProtList()).getResult();
					completeDavid(DavidManager.getDavidManager().getDavidSet(),NrdManager.getNrdManager().getNrdList(),UniProtManager.getUniProtManager().getUniProtList());
					// 写入excel
					ExcelUtil.writeExcel("Uniprot_Acc_Number_Mapping", UniProtAcc.filedList, UniProtAccManager.getUniProtAccManager().getUniProtAccList(), AIM_FILE_PATH);
					// 画图
					DrawingUtil.drawingPic(UniProtAccManager.getUniProtAccManager().getUniProtAccList(), PIC_FILE_PATH);
					ExcelUtil.insertPicToExcelAndData(PIC_FILE_PATH, AIM_FILE_PATH, "Species Distribution", SpeciesDistribution.getFiledList(), SpeciesDistribution.getDataMap());

					ExcelUtil.writeSheet3ToExcel(AIM_FILE_PATH, "Unique_Entry", David.getFiledList(), DavidManager.getDavidManager().getSortedDavidList());
					isWrite = true;
					return;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else {
			//TO-DO
		}
	}

	private static void init(){
		if (ORIGIN_FILE_PATH != null && !"".equals(ORIGIN_FILE_PATH)) {
			ExcelUtil.readCSVToReactome(ORIGIN_FILE_PATH);
		}
		if (uniprotService != null) {
			//开启服务
			uniprotService.start();
		}
		//将nrd excel中的内容读取到管理类中
		ExcelUtil.readEecelToManager(ORIGIN_NRD_FILE_PATH);
	}

	/**
	 * 完善标签和hosts
	 * @param davids
	 * @param nrds
	 * @param uniProts
	 */
	private static void completeDavid(Set<David> davids, List<Nrd> nrds, List<UniProt> uniProts){
		for (David david : davids) {
			for (Nrd nrd : nrds) {
				if (david.getEntry().equals(nrd.getEntry())) {
					david.setLabel(nrd.getLabel());
					break;
				}
			}
			for (UniProt uniProt : uniProts) {
				if (david.getEntry().equals(uniProt.getEntry())) {
					david.setHosts(Integer.parseInt(uniProt.getUniqueUsers()));
					break;
				}
			}
		}
	}
	/**
	 * 定时给uniprot服务器发送请求
	 */
	private static class SendInfoToUniProt implements Runnable {
		@Override
		public void run() {
			while (!isLast && !isWrite) {
				List<Reactome> reactomeList = ReactomeManager.getReactomeManager().getReactomeList();
				Reactome reactome = reactomeList.get(i++);
				logger.debug(i+"当前term : "+reactome.getTerm());
				logger.debug("当前查询到的总数 : "+UniProtAccManager.getUniProtAccManager().getUniProtAccList());
				try {
//						uniprotService.start();
					List<UniProt> uniProtList = UniProtSearchUtil.searchInfoEntries(reactome, uniprotService);
					// 将查询到的结果集添加到管理类
					UniProtManager.getUniProtManager().getUniProtList().addAll(uniProtList);
					logger.debug("查询到的结果： "+uniProtList);
					if (uniProtList.size() != 0) {
						UniProtAcc uniProtAcc = new UniProtAcc(uniProtList.get(0).getTerm());
						if (checkList(uniProtList)) {
							uniProtAcc.setEntry(uniProtList.get(0).getEntry());
							uniProtAcc.setOrganism(uniProtList.get(0).getOrganism());
						} else {
							uniProtAcc.setOrganism("Non-unique");
							String entry = "";
							for (UniProt uniProt : uniProtList) {
								entry += uniProt.getEntry() + ",";
							}
							uniProtAcc.setEntry(entry.substring(0, entry.length() - 1));
						}
						UniProtAccManager.getUniProtAccManager().getUniProtAccList().add(uniProtAcc);
						logger.debug("最终的uniProtAcc为： "+uniProtAcc);
					}
				} catch (ServiceException e) {
					e.printStackTrace();
				}
				//停顿1秒
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {

				}
			}
		}

		/**
		 * 检查集合中是否有只有一种
		 * @param uniProtList
		 * @return
		 */
		private static boolean checkList(List<UniProt> uniProtList) {
			if (uniProtList == null || uniProtList.size() == 0){
				return false;
			}
			Set<String> set = new HashSet<>();
			for (UniProt uniProt : uniProtList) {
				set.add(uniProt.getOrganism());
			}
			return set.size() == 1;
		}
	}

	private static class CheckIsFileLast implements Runnable {
		@Override
		public void run() {
			if (ReactomeManager.getReactomeManager().getReactomeList().size() - 1 <= i) {
				isLast = true;
				logger.debug("已经检测到达底部");
			}
			logger.debug(ReactomeManager.getReactomeManager().getReactomeList().size()+"还没到达底部哦到达底部"+i);
		}
	}

}
