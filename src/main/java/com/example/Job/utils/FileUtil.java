package com.example.Job.utils;

import com.example.Job.exception.StorageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUtil {
    public static boolean isSupportedFileExtension(MultipartFile file, String[] supportedExtensions) {
        if (file == null || file.isEmpty()) {
            return false;
        }

        String fileName = Objects.requireNonNull(file.getOriginalFilename()).toLowerCase();
        List<String> allowedExtensions = Arrays.asList(supportedExtensions);

        boolean isValid =  allowedExtensions.stream().anyMatch(extension -> fileName.endsWith(extension));

//        if(!isValid){
//            throw new StorageException("Only file type " + allowedExtensions +  " are allowed");
//        }
        return isValid;
    }
}
