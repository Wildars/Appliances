package com.example.appliances.service.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class MediaTypeUtils {
//    public static String uploadPathImage = "C:/Users/xosha/IdeaProjects/proremont/src/main/resources/img/";

    public static String uploadPathImage = "/opt/img/";
    public static String uploadPathSlide = "/opt/sliders/";


//    public static String uploadPathImage = "C:/Users/Kantemir/IdeaProjects/proremont_back/src/main/resources/img/";
//    public static String uploadPathSlide = "C:/Users/Kantemir/IdeaProjects/proremont_back/src/main/resources/sliders/";



    public static List<String> saveImages(List<MultipartFile> files) {
        List<String> resultUrls = new ArrayList<>();
        try {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    String uuidFile = UUID.randomUUID().toString();
                    String resultFilename = uuidFile + "-" + file.getOriginalFilename();
                    file.transferTo(new File(uploadPathImage + resultFilename));
//                    String url = baseUrl + resultFilename; // Формирование URL-адреса фотографии
                    resultUrls.add(resultFilename);
                }
            }
            return resultUrls;
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return Collections.emptyList();
        }
    }


    public static String saveSlide(MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "-" + file.getOriginalFilename();
                file.transferTo(new File(uploadPathSlide + resultFilename));
                return resultFilename;
            } else {
                return null; // Возвращаем null если файл пустой или не существует
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            return null; // Возвращаем null в случае ошибки
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
