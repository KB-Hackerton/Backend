package kb_hack.backend.global.s3.service;

import io.awspring.cloud.s3.S3Template;
import kb_hack.backend.global.s3.mapper.S3Mapper;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Template s3Template;
    private final S3Mapper s3Mapper;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public String uploadFile(MultipartFile file) throws IOException {
        MemberVO vo = getMemberVO();
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

        s3Template.upload(bucket, key, file.getInputStream());
        String url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

        s3Mapper.insertProfileImageByUser(url, vo.getMemberId());
        return url;
    }


    private static MemberVO getMemberVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityCustomUser securityUser = (SecurityCustomUser) authentication.getPrincipal();
        MemberVO vo = securityUser.getMemberVO();
        return vo;
    }
}

