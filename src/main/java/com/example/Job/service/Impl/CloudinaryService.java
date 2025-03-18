package com.example.Job.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.Job.service.interfaces.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService implements IStorageService {

    private Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public Object uploadResumes(MultipartFile file, String folder, String fileName) {
        try{
            Map uploadedFile = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", folder,
                            "public_id", fileName,
                            "resource_type", "raw"
                    )
            );
            return uploadedFile.get("url");

        } catch (IOException e) {
            return "Upload failed";
        }


    }
}
