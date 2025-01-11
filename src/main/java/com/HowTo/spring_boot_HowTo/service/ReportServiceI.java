package com.HowTo.spring_boot_HowTo.service;

import java.util.List;

import com.HowTo.spring_boot_HowTo.model.Report;

public interface ReportServiceI {
	
List<Report> getAllReports();
	
	Report saveTutorialReport(Report report, Long userId, Long tutorialId);
	
	Report saveUserReport(Report report, Long userId);
	
	Report getReportById(Long id);
	
	Report updateReport(Report report);
	
	void delete(Report report);
}
