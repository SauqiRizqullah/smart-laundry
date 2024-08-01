package com.gruptiga.smartlaundry.service.impl;

import com.gruptiga.smartlaundry.entity.Image;
import com.gruptiga.smartlaundry.repository.ImageRepository;
import com.gruptiga.smartlaundry.service.ImageService;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.exceptions.*;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final Path directoryPath;
    private final ImageKit imageKit;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository,
                            @Value("${save_it.multipart.path_location}") String directoryPath,
                            ImageKit imageKit) {
        this.imageRepository = imageRepository;
        this.directoryPath = Paths.get(directoryPath);
        this.imageKit = imageKit;
    }

    @PostConstruct
    public void initDirectory() {
        if (!Files.exists(directoryPath)) {
            try {
                Files.createDirectory(directoryPath);
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    @Override
    public Image create(MultipartFile multipartFile) {

        // cek format file yang diupload
        try {
            if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml").contains(multipartFile.getContentType())) {
                throw new ConstraintViolationException("Invalid image format", null);
            }

            // inisiasi nama file

            byte[] fileBytes = multipartFile.getBytes();
            String base64File = Base64.getEncoder().encodeToString(fileBytes);

            FileCreateRequest request = new FileCreateRequest(base64File, multipartFile.getOriginalFilename());

            // menetapkan folder upload

            request.setFolder("/test");

            // memanfaatkan imageKit untuk upload gambar

            Result result = imageKit.upload(request);

            //

            String imageUrl = result.getUrl();
            String modifiedUrl = imageUrl.replaceFirst("^https?://", "");

            if (result != null && result.getUrl() != null) {
                Image image = Image.builder()
                        .imageName(multipartFile.getOriginalFilename())
                        .size(multipartFile.getSize())
                        .contentType(multipartFile.getContentType())
                        .path(modifiedUrl)
                        .build();
                imageRepository.saveAndFlush(image);
                return image;
            } else {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image");
            }
        } catch (IOException | ForbiddenException | TooManyRequestsException | InternalServerException |
                 UnauthorizedException | BadRequestException | UnknownException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Resource getById(String id) {
        try {
            Image image = imageRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"));
            Path imagePath = Paths.get(image.getPath());
            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found");
            }
            return new UrlResource(image.getPath());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    @Override
    public Image searchById(String id) {
        Optional<Image> searchById = imageRepository.findById(id);
        return searchById.orElse(null);
    }

    @Override
    public Image searchByName(String imageName) {
        return imageRepository.searchName(imageName).orElse(null);
    }

    @Override
    public void delete(String id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Data not found"));

        Path imagePath = Paths.get(image.getPath());
        try {
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
            }
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        imageRepository.delete(image);
    }

    @Override
    public List<Image> searchAll() {
        return imageRepository.findAll();
    }
}
