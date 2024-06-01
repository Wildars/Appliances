package com.example.appliances.service.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MediaTypeUtils {
//    public static String uploadPathImage = "C:/Users/Kantemir/IdeaProjects/Appliances/src/main/resources/img/";

    public static String uploadPathImage = "/opt/photoAppliances/";
    public static List<String> saveImages(List<MultipartFile> files) {
        List<String> resultUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + "-" + file.getOriginalFilename();
                    file.transferTo(new File(uploadPathImage + resultFilename));
                    resultUrls.add(resultFilename);
                }
            }
            return resultUrls;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return Collections.emptyList();
        }
    }

    public static Boolean deleteImage(String fileName) {
        try {
            if (fileName != null && fileName.length() > 0) {
                File imageForDelete = new File(uploadPathImage + fileName);
                imageForDelete.delete();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
