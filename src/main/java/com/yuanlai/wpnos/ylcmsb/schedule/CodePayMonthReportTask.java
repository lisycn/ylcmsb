package com.yuanlai.wpnos.ylcmsb.schedule;

import com.yuanlai.wpnos.ylcmsb.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



/**
 * @author Lee
 *
 */
public class CodePayMonthReportTask {
	
	private static Logger log = LoggerFactory.getLogger(CodePayMonthReportTask.class);

	@Autowired
	private ReportService reportService;

	public void run() {
		log.debug("每月1日生成上月交易报表   Task Start");

		try {
			reportService.monthReportTask();
		} catch (Exception e) {
			log.error("每月1日生成上月交易报表  Exception:  " + e.getMessage());
			e.printStackTrace();
		}
		log.debug("每月1日生成上月交易报表   Task End");
	}

}
