package com.example.Job.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {
    public Object uploadResumes(MultipartFile file, String folder, String fileName);
}
