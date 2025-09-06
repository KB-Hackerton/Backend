package kb_hack.backend.domain.sos.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StorageServiceStub implements StorageService {
	@Override
	public List<String> uploadAll(List<MultipartFile> files) {
		if (files == null || files.isEmpty()) return List.of();
		// TODO: 실제 S3 업로드 구현 (파일명/경로 정책 포함)
		return files.stream()
			.map(f -> "sos/" + UUID.randomUUID() + "_" + f.getOriginalFilename())
			.collect(Collectors.toList());
	}
}
