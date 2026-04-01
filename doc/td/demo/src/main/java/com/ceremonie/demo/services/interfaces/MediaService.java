package com.ceremonie.demo.services.interfaces;


import org.springframework.web.multipart.MultipartFile;

import com.ceremonie.demo.dto.request.CreateMediaRequest;
import com.ceremonie.demo.dto.response.MediaResponse;
import com.ceremonie.demo.enums.MediaType;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    MediaResponse uploadMedia(MultipartFile file, CreateMediaRequest request) throws IOException;
    MediaResponse getMediaById(Long id);
    List<MediaResponse> getMediasByYear(Long yearId);
    List<MediaResponse> getMediasByActiveYear();
    List<MediaResponse> getMediasByType(MediaType type);
    List<MediaResponse> searchMedias(String search);
    byte[] downloadMedia(Long id) throws IOException;
    void deleteMedia(Long id);
}
