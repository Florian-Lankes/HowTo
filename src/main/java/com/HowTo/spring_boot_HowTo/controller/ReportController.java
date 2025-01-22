package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.model.Report;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.ReportServiceI;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/report")
public class ReportController {

	private ReportServiceI reportService;
	private UserServiceI userService;
	private TutorialServiceI tutorialService;
	private static final Logger logger = LogManager.getLogger(GroupController.class);
	

	public ReportController(ReportServiceI reportService, TutorialServiceI tutorialService, UserServiceI userService) {
		super();
		this.reportService = reportService;
		this.userService = userService;
		this.tutorialService = tutorialService;
	}

	private Long getCurrentUserId() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()
				|| authentication.getPrincipal() instanceof String) {
			throw new IllegalStateException("User is not authenticated");
		}
		User userDetails = (User) authentication.getPrincipal();
		return userDetails.getUserId();
	}
	//shows all reports for admin
	@GetMapping("/all")
	public String showReports(Model model) {
		logger.info("Entering showReports method");
		List<Report> AllReports = reportService.getAllReports();
		model.addAttribute("reports", AllReports);
		logger.info("All reports retrieved and added to model");
		return "reports/report-list";
	}
	// deletes the selected report
	@GetMapping("/delete/{id}")
	public String deleteReport(@PathVariable("id") Long reportId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
		logger.info("Entering deleteReport method with reportId: {}", reportId);
		Report report = reportService.getReportById(reportId);
		reportService.delete(report);
		String referer = request.getHeader("Referer");
		redirectAttributes.addFlashAttribute("deleted", "Report deleted!");
		logger.info("Report deleted successfully with reportId: {}", reportId);
		return "redirect:"+ referer;
	}
	//Reporting is split between channel report and tutorial report
	
	//shows create page for channel report 
	@GetMapping("/user/{id}")
	public String reportUserView(@PathVariable("id") Long userId, Model model) {
		logger.info("Entering reportUserView method with userId: {}", userId);
		Report report = new Report();
		model.addAttribute("userId", userId);
		model.addAttribute("report", report);
		logger.info("Report form created and added to model for userId: {}", userId);
		return "reports/report-user";
	}
	//creates the report done on previous page
	@PostMapping("/user/{id}")
	public String reportUser(@PathVariable("id") Long userId, @Valid @ModelAttribute Report report, BindingResult result,  Model model) {
		if (result.hasErrors()) {
			model.addAttribute("userId", userId);
			logger.error("Validation errors: {}", result.getAllErrors());
			return "reports/report-user";
		}
		logger.info("Entering reportUser method with userId: {}", userId);
		reportService.saveUserReport(report, userId);
		logger.info("User report saved successfully for userId: {}", userId);
		return "redirect:/channel/view/" + userId;
	}
	//shows create page for tutorial report
	@GetMapping("/tutorial/{id}")
	public String reportTutorialView(@PathVariable("id") Long tutorialId, Model model) {
		logger.info("Entering reportTutorialView method with tutorialId: {}", tutorialId);
		Report report = new Report();
		model.addAttribute("tutorialId", tutorialId);
		model.addAttribute("report", report);
		logger.info("Report form created and added to model for tutorialId: {}", tutorialId);
		return "reports/report-tutorial";
	}
	//creates the report done on previous page
	@PostMapping("/tutorial/{id}")
	public String reportTutorial(@PathVariable("id") Long tutorialId, @Valid @ModelAttribute Report report, BindingResult result , Model model) {
		if (result.hasErrors()) {
			model.addAttribute("tutorialId", tutorialId);
			logger.error("Validation errors: {}", result.getAllErrors());
			return "reports/report-tutorial";
		}
		logger.info("Entering reportTutorial method with tuto rialId: {}", tutorialId);
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
		reportService.saveTutorialReport(report, tutorial.getCreatedByChannel().getChannelId(), tutorialId);
		logger.info("Tutorial report saved successfully for tutorialId: {}", tutorialId);
		return "redirect:/tutorial/view/" + tutorialId;
	}
}
