package com.dingdong.api.s3.controller;


import com.dingdong.api.s3.controller.request.DeleteImageRequest;
import com.dingdong.api.s3.controller.response.UploadImageResponse;
import com.dingdong.infrastructure.s3.IS3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "이미지 업로드")
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class S3Controller {

    private final IS3Service s3Service;

    @Operation(summary = "이미지 업로드")
    @PostMapping(path = "/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadImageResponse uploadImage(@RequestParam("image") MultipartFile multipartFile)
            throws Exception {
        return UploadImageResponse.from(s3Service.uploadImage(multipartFile));
    }

    @Operation(summary = "이미지 삭제")
    @DeleteMapping("/image")
    public void remove(@RequestBody DeleteImageRequest request) throws Exception {
        s3Service.remove(request.getImageUrl());
    }
}
