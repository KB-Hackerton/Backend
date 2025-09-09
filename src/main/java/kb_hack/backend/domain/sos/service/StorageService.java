package kb_hack.backend.domain.sos.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface StorageService {
	// 업로드 후 "storage_key" 들을 반환 (예: "sos/2025/09/01/uuid.jpg")
	List<String> uploadAll(List<MultipartFile> files,Long sosId);
}
