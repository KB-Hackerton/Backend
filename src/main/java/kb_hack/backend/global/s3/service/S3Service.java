package kb_hack.backend.global.s3.service;

import io.awspring.cloud.s3.S3Template;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import kb_hack.backend.global.s3.mapper.S3Mapper;
import kb_hack.backend.global.security.dto.SecurityCustomUser;
import kb_hack.backend.global.security.entity.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class S3Service {

    private final S3Template s3Template;
    private final S3Mapper s3Mapper;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.region.static}")
    private String region;

    public String uploadFile(MultipartFile file) {
        try {
            MemberVO vo = getMemberVO();


            if (file == null || file.isEmpty()) {
                throw new BadRequestException(BadStatusCode.INVALID_FILE_UPLOAD_EXCEPTION); // 빈 파일 업로드
            }

            String key = UUID.randomUUID() + "_" + file.getOriginalFilename();

            s3Template.upload(bucket, key, file.getInputStream());
            String url = "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;


            int result = s3Mapper.insertProfileImageByUser(url, vo.getMemberId());
            if (result <= 0) {
                throw new ServerErrorException(BadStatusCode.DATABASE_PROCESSING_EXCEPTION);
            }

            return url;

        } catch (IOException e) {
            throw new ServerErrorException(BadStatusCode.FILE_UPLOAD_FAILED_EXCEPTION);
        } catch (ServerErrorException e) {
            throw e; // 커스텀 예외는 그대로 던짐
        } catch (Exception e) {
            throw new ServerErrorException(BadStatusCode.FAIL_TO_PROCESSING_FILE_UPLOAD_EXCEPTION);
        }
    }

    private static MemberVO getMemberVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SecurityCustomUser)) {
            return null;
        }
        return ((SecurityCustomUser) authentication.getPrincipal()).getMemberVO();
    }
}
