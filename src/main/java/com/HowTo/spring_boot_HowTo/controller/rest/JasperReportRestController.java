package com.HowTo.spring_boot_HowTo.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.HowTo.spring_boot_HowTo.service.JasperReportServiceI;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@RestController
@RequestMapping("/api/report")
public class JasperReportRestController {

    @Autowired
    private JasperReportServiceI jasperReportService;

    //creates pdf of selected tutorial for download
    @GetMapping("/pdf/{id}")
    public ResponseEntity<InputStreamResource> generateReport(@PathVariable("id") Long tutorialId) throws Exception {
        ByteArrayOutputStream pdfStream = jasperReportService.generatePdfReport(tutorialId);

        InputStream inputStream = new ByteArrayInputStream(pdfStream.toByteArray());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=report.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(inputStream));
    }
}
