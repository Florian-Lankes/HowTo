package com.HowTo.spring_boot_HowTo.service;

import java.io.ByteArrayOutputStream;


public interface JasperReportServiceI {
	
	
	public ByteArrayOutputStream generatePdfReport(long tutorialId) throws Exception;
}