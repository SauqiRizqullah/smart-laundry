package com.gruptiga.smartlaundry.controller;

import com.gruptiga.smartlaundry.constant.APIUrl;
import com.gruptiga.smartlaundry.entity.Image;
import com.gruptiga.smartlaundry.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = APIUrl.IMAGE)
public class ImageController {
    private final ImageService imageService;

    @PostMapping(produces = "application/json")
    ResponseEntity<Image> create(
            @RequestPart(name = "image") MultipartFile multipartFile){
        Image image = imageService.create(multipartFile);
        return ResponseEntity.status(HttpStatus.CREATED).body(image);
    }

    @GetMapping(path = APIUrl.PATH_VAR_IMAGE_ID, produces = "application/json")
    ResponseEntity<Image> getById(
            @PathVariable(name = "imageId") String imageId
    ){
        Image image = imageService.searchById(imageId);

        return ResponseEntity.status(HttpStatus.OK).body(image);
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<Image>> getAllImages(){
        List<Image> allImages = imageService.searchAll();
        return ResponseEntity.status(HttpStatus.OK).body(allImages);
    }

    @GetMapping(path = "/search", produces = "application/json")
    ResponseEntity<Image> getByName(
            @RequestParam(name = "imageName") String imageName
    ){
        Image image = imageService.searchByName(imageName);

        return ResponseEntity.status(HttpStatus.OK).body(image);
    }
}
