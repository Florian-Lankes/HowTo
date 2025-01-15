package com.HowTo.spring_boot_HowTo.service.impl;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.service.JasperReportServiceI;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class JasperReportService implements JasperReportServiceI{

    public ByteArrayOutputStream generatePdfReport(long tutorialId) throws Exception {
        // 1. API-Daten abrufen
        String apiUrl = "http://localhost:8080/api/tutorials/" + tutorialId;
        System.out.println(apiUrl);
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json"); // Setzen des Accept Headers
        conn.setRequestProperty("Content-Type", "application/json"); // Setzen des Content-Type Headers
        
        // Basic Authentication hinzufügen 
        String userCredentials = "user3:1234567";
        String basicAuth = "Basic " + new String(Base64.getEncoder().encode(userCredentials.getBytes())); 
        conn.setRequestProperty("Authorization", basicAuth);
        
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuilder content = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        conn.disconnect();

        // 2. JSON-Daten verarbeiten
        JSONObject jsonObject = new JSONObject(content.toString());
        List<Tutorial> dataList = new ArrayList<>();
        Tutorial bean = new Tutorial();
        bean.setTitle(jsonObject.getString("title"));
        bean.setContentText(jsonObject.getString("contentText"));
        Date date = new java.util.Date(jsonObject.getLong("creationTime"));
        Timestamp ts = new Timestamp(date.getTime());
        bean.setCreationTime(ts); dataList.add(bean);

        // 3. Bericht erstellen und ausfüllen
        InputStream reportStream = getClass().getResourceAsStream("/templates/report.jrxml");
        if (reportStream == null) 
        	{ throw new FileNotFoundException("Report template not found in classpath: /templates/report.jrxml");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        Map<String, Object> parameters = new HashMap<>();
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 4. PDF exportieren
        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, pdfStream);
        
        return pdfStream;
    }
    
    
}
