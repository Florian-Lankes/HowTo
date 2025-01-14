package com.HowTo.spring_boot_HowTo.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.HowTo.spring_boot_HowTo.config.MyUserDetails;
import com.HowTo.spring_boot_HowTo.model.Report;
import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.model.User;
import com.HowTo.spring_boot_HowTo.service.ReportServiceI;
import com.HowTo.spring_boot_HowTo.service.TutorialServiceI;
import com.HowTo.spring_boot_HowTo.service.UserServiceI;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/report")
public class ReportController {

	private ReportServiceI reportService;
	private UserServiceI userService;
	private TutorialServiceI tutorialService;

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
		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		return userDetails.getId();
	}

	@GetMapping("/all")
	public String showReports(Model model) {

		List<Report> AllReports = reportService.getAllReports();
		model.addAttribute("reports", AllReports);

		return "reports/report-list";
	}

	@GetMapping("/delete/{id}")
	public String deleteReport(@PathVariable("id") Long reportId, RedirectAttributes redirectAttributes) {
		Report report = reportService.getReportById(reportId);
		reportService.delete(report);
		redirectAttributes.addFlashAttribute("deleted", "Report deleted!");
		return "redirect:/report/all";
	}

	@GetMapping("/user/{id}")
	public String reportUserView(@PathVariable("id") Long userId, Model model) {
		Report report = new Report();
		model.addAttribute("userId", userId);
		model.addAttribute("report", report);

		return "reports/report-user";
	}

	@PostMapping("/user/{id}")
	public String reportUser(@PathVariable("id") Long userId, @Valid @ModelAttribute Report report) {
		reportService.saveUserReport(report, userId);
		return "redirect:/channel/view/" + userId;
	}

	@GetMapping("/tutorial/{id}")
	public String reportTutorialView(@PathVariable("id") Long tutorialId, Model model) {
		Report report = new Report();
		model.addAttribute("tutorialId", tutorialId);
		model.addAttribute("report", report);
		return "reports/report-tutorial";
	}

	@PostMapping("/tutorial/{id}")
	public String reportTutorial(@PathVariable("id") Long tutorialId, @Valid @ModelAttribute Report report) {
		Tutorial tutorial = tutorialService.getTutorialById(tutorialId);
		reportService.saveTutorialReport(report, tutorial.getCreatedByChannel().getChannelId(), tutorialId);
		return "redirect:/tutorial/view/" + tutorialId;
	}

}
