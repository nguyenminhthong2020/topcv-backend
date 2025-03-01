package com.example.Job.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.cloudinary.api.ApiResponse;
import java.util.List;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "*")
public class CloudinaryController {

    @Autowired
    private Cloudinary cloudinary;

    // Cố định folder upload là "upload/image/avatar"
    private static final String UPLOAD_FOLDER = "upload/image/avatar";

    // @PostMapping("/{folder}")
    // @PreAuthorize("isAuthenticated()")
    // public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile
    // file, @PathVariable String folder) {
    // try {
    // Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
    // ObjectUtils.asMap("folder", folder));
    // return ResponseEntity.ok(uploadResult);
    // } catch (IOException e) {
    // return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload
    // failed");
    // }
    // }

    @PostMapping("/avatar")
    @PreAuthorize("isAuthenticated()") // Yêu cầu có JWT Token
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Lấy tên gốc của file
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File không hợp lệ");
            }

            // Lấy phần mở rộng file (.jpg, .png, ...)
            String extension = "";
            int dotIndex = originalFilename.lastIndexOf(".");
            if (dotIndex != -1) {
                extension = originalFilename.substring(dotIndex);
                originalFilename = originalFilename.substring(0, dotIndex); // Bỏ phần mở rộng
            }

            // Tạo tên file mới để tránh trùng (VD: "granola_abc123.jpg")
            String uniqueFilename = originalFilename + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            var uploadResult = cloudinary.uploader()
                    .upload(file.getBytes(),
                            ObjectUtils
                                    .asMap("folder", UPLOAD_FOLDER,
                                            "public_id", uniqueFilename));
            return ResponseEntity.ok(uploadResult);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    // API tìm ảnh theo tên (chứa keyword)
    @GetMapping("/search")
    public ResponseEntity<?> searchImages(@RequestParam("name") String name) {
        try {
            ApiResponse result = cloudinary.search()
                    .expression("resource_type:image AND folder:" + UPLOAD_FOLDER)
                    .execute();

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> images = (List<Map<String, Object>>) result.get("resources");

            // Lọc ra ảnh có chứa keyword trong tên file
            List<String> matchingUrls = images.stream()
                    .filter(img -> img.get("filename").toString().toLowerCase().contains(name.toLowerCase()))
                    .map(img -> img.get("secure_url").toString()) // Lấy URL ảnh
                    .collect(Collectors.toList());

            if (matchingUrls.isEmpty()) {
                return ResponseEntity.ok("No images found");
            }
            return ResponseEntity.ok(matchingUrls);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error searching images");
        }
    }
}
