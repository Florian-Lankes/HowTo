package com.HowTo.spring_boot_HowTo.service;

import org.springframework.web.multipart.MultipartFile;


public interface CloudinaryServiceI {
	
	
	String uploadFile(MultipartFile file, Long tutorialId );
	
	void deleteFile(String publicId);
	
	void deleteVideoUrl(Long tutorialId);
	
	String uploadFileAdvertisement(MultipartFile file, Long advertisementId);
	
	void deleteFileAdvertisement(String publicId) ;
	
	void deleteVideoUrlAdvertisement(Long advertisementId) ;
}