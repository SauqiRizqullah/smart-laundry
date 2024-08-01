package com.gruptiga.smartlaundry.service;

import com.gruptiga.smartlaundry.entity.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    Image create(MultipartFile multipartFile);

    Resource getById(String id);

    Image searchById(String id);

    Image searchByName(String imageName);

    void delete(String id);

    List<Image> searchAll();
}
