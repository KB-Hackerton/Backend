package kb_hack.backend.global.s3.controller;

import com.google.common.net.HttpHeaders;
import kb_hack.backend.global.common.exception.enums.SuccessStatusCode;
import kb_hack.backend.global.common.response.success.SuccessResponse;
import kb_hack.backend.global.s3.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/upload")
    public SuccessResponse<String> upload(@RequestPart("file") MultipartFile file) throws IOException {
        String key = s3Service.uploadFile(file);
        return SuccessResponse.makeResponse(SuccessStatusCode.IMAGE_UPLOAD_SUCCESS, s3Service.uploadFile(file));
    }

}
