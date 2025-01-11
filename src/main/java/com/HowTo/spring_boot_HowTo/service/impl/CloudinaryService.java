package com.HowTo.spring_boot_HowTo.service.impl;

import com.HowTo.spring_boot_HowTo.model.Tutorial;
import com.HowTo.spring_boot_HowTo.repository.TutorialRepositoryI;
import com.HowTo.spring_boot_HowTo.service.CloudinaryServiceI;
import com.cloudinary.Cloudinary;
import com.cloudinary.EagerTransformation;
import com.cloudinary.utils.ObjectUtils;

import jakarta.annotation.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CloudinaryService implements CloudinaryServiceI {

	@Resource
	private Cloudinary cloudinary;

	@Autowired
	TutorialRepositoryI tutorialRepository;

	@Override
	public String uploadFile(MultipartFile file, Long tutorialId) {
		Tutorial tutorial = tutorialRepository.findById(tutorialId).get();
		if (tutorial != null) {
			try {
				Map uploadResult = cloudinary.uploader().upload(file.getBytes() , 
					    ObjectUtils.asMap("resource_type", "video",
					    	    "eager", Arrays.asList(
					    	        new EagerTransformation().width(300).height(300).crop("pad").audioCodec("none"),
					    	        new EagerTransformation().width(160).height(100).crop("crop").gravity("south").audioCodec("none")),
					    	    "eager_async", true));
						//upload(file.getBytes(), ObjectUtils.emptyMap());
				String url = uploadResult.get("secure_url").toString();
				tutorial.setVideoUrl(url);
				tutorialRepository.save(tutorial);
				return url;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("error cloudinary!");
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
	
	
	
	@Override
	public void deleteFile(String publicId) {
		
		try {
			Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type","video"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void deleteVideoUrl(Long tutorialId) {
		Tutorial tutorial = tutorialRepository.findById(tutorialId).get();
		tutorial.setVideoUrl(null);
		tutorialRepository.save(tutorial);
	}
}
