package com.HowTo.spring_boot_HowTo.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.model.Report;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.UserRepositoryI;
import com.HowTo.spring_boot_HowTo.repository.ReportRepositoryI;
import com.HowTo.spring_boot_HowTo.service.ReportServiceI;

@Service
public class ReportService implements ReportServiceI{

	@Autowired
	ReportRepositoryI reportRepository;
	
	@Autowired
	UserRepositoryI userRepository;
	
	@Autowired
	TutorialRepositoryI tutorialRepository;
	
	@Override
	public List<Report> getAllReports() {
		// TODO Auto-generated method stub
		return reportRepository.findAll();
	}

	@Override
	public Report saveTutorialReport(Report report, Long userId, Long tutorialId) {
		
		User user = userRepository.findById(userId).get();
		Tutorial tutorial = tutorialRepository.findById(tutorialId).get();
		List<Report> reports = user.getReports();
		
		if(user != null  && reports != null && tutorial != null) {
			if(!reports.contains(report)) {
				report.setReportUser(user);
				report.setReportTutorial(tutorial);
				user.addReport(report);
				tutorial.addReport(report);
				reportRepository.save(report);
			}
		}
		return report;
	}
	
	@Override
	public Report saveUserReport(Report report, Long userId) {
		User user = userRepository.findById(userId).get();
		List<Report> reports = user.getReports();
		
		if(user != null  && reports != null ) {
			if(!reports.contains(report)) {
				report.setReportUser(user);
				user.addReport(report);
				reportRepository.save(report);
			}
		}
		return report;
	}

	@Override
	public Report getReportById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Report updateReport(Report report) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Report report) {
		// TODO Auto-generated method stub
		
	}

	
	
	
}
