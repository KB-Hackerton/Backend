package kb_hack.backend.domain.sos.service;

import io.awspring.cloud.s3.S3Template;
import kb_hack.backend.domain.sos.entity.SosImage;
import kb_hack.backend.domain.sos.mapper.SosImageMapper;
import kb_hack.backend.global.common.exception.enums.BadStatusCode;
import kb_hack.backend.global.common.exception.type.BadRequestException;
import kb_hack.backend.global.common.exception.type.ServerErrorException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StorageServiceStub implements StorageService {

	private final S3Template s3Template;
	private final SosImageMapper sosImageMapper;

	@Value("${spring.cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${spring.cloud.aws.region.static}")
	private String region;

	@Override
	public List<String> uploadAll(List<MultipartFile> files, Long sosId) {
		if (files == null || files.isEmpty()) return List.of();

		return files.stream().map(file -> {
			try {
				if (file == null || file.isEmpty()) {
					throw new BadRequestException(BadStatusCode.INVALID_FILE_UPLOAD_EXCEPTION);
				}


				String key = "sos/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
				s3Template.upload(bucket, key, file.getInputStream());

				return "https://" + bucket + ".s3." + region + ".amazonaws.com/" + key;

			} catch (IOException e) {
				throw new ServerErrorException(BadStatusCode.FILE_UPLOAD_FAILED_EXCEPTION);
			} catch (ServerErrorException e) {
				throw e;
			} catch (Exception e) {
				throw new ServerErrorException(BadStatusCode.FAIL_TO_PROCESSING_FILE_UPLOAD_EXCEPTION);
			}
		}).collect(Collectors.toList());
	}
}

