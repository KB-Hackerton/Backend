package kb_hack.backend.domain.sos.service;

import kb_hack.backend.domain.sos.dto.SosCreateRequest;
import kb_hack.backend.domain.sos.dto.SosCreateResponse;
import kb_hack.backend.domain.sos.dto.SosDetailResponse;
import kb_hack.backend.domain.sos.dto.SosDetailRow;
import kb_hack.backend.domain.sos.dto.SosListResponse;
import kb_hack.backend.domain.sos.entity.Sos;
import kb_hack.backend.domain.sos.entity.SosImage;
import kb_hack.backend.domain.sos.entity.SosType;
import kb_hack.backend.domain.sos.mapper.SosImageMapper;
import kb_hack.backend.domain.sos.mapper.SosMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import kb_hack.backend.domain.sos.dto.SosDetailRow;


@Service
@RequiredArgsConstructor
public class SosServiceImpl implements SosService {

	private final SosMapper sosMapper;
	private final SosImageMapper sosImageMapper;
	private final StorageService storageService;

	// 지원하는 날짜 포맷
	private static final List<DateTimeFormatter> EXPIRES_FORMATS = List.of(
		DateTimeFormatter.ISO_DATE_TIME,              // 예: 2025-09-01T23:59:00
		DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm") // 예: 2025-09-01 23:59
	);

	private LocalDateTime parseExpiresAt(String input) {
		try {
			DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
			return LocalDateTime.of(
				LocalDate.now(),                // 오늘 날짜
				LocalTime.parse(input, timeFormatter) // 입력 시간
			);
		} catch (Exception e) {
			throw new IllegalArgumentException("expires_at 형식이 올바르지 않습니다. 예) 23:59");
		}
	}


	@Override
	@Transactional
	public SosCreateResponse create(SosCreateRequest req) {
		// 1) 만료일 파싱
		LocalDateTime expiresAt = parseExpiresAt(req.getExpiresAt());

		// 2) SOS row 생성
		Sos sos = Sos.builder()
			.memberId(req.getMemberId())
			.sosTitle(req.getSosTitle())
			.sosType(req.getSosType())
			.sosContent(req.getSosContent())
			.expiresAt(expiresAt)
			.isComplete(false)
			.isDeleted(false)
			.build();
		sosMapper.insert(sos); // generatedKeys로 sosId 세팅됨

		// 3) 이미지 업로드 (storage_key 리스트 생성)
		List<MultipartFile> files = req.getImages();
		List<String> keys = storageService.uploadAll(files, sos.getSosId());

		// 4) sos_image 저장
		for (String key : keys) {
			SosImage image = SosImage.builder()
				.sosId(sos.getSosId())
				.storageKey(key)
				.isDeleted(false)
				.build();
			sosImageMapper.insert(image);
		}

		// 5) 응답 DTO 생성
		return SosCreateResponse.builder()
			.sosId(sos.getSosId())
			.imageKeys(keys)
			.build();
	}

	@Override
	@Transactional
	public void update(Long sosId, SosCreateRequest req) {
		// 만료일 파싱
		LocalDateTime expiresAt = parseExpiresAt(req.getExpiresAt());

		Sos sos = Sos.builder()
			.sosId(sosId)
			.sosTitle(req.getSosTitle())
			.sosType(req.getSosType())
			.sosContent(req.getSosContent())
			.expiresAt(expiresAt)
			.build();

		int updated = sosMapper.update(sos);
		if (updated == 0) {
			throw new IllegalArgumentException("해당 SOS가 존재하지 않거나 삭제된 상태입니다.");
		}
	}
	@Override
	@Transactional
	public void hardDelete(Long sosId) {
		int deleted = sosMapper.hardDelete(sosId);
		if (deleted == 0) {
			throw new IllegalArgumentException("존재하지 않는 SOS ID: " + sosId);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<SosListResponse> getSosList() {
		return sosMapper.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public SosDetailResponse getSosDetail(Long sosId) {
		List<SosDetailRow> rows = sosMapper.findDetail(sosId);
		if (rows.isEmpty()) {
			throw new IllegalArgumentException("존재하지 않는 SOS ID: " + sosId);
		}

		SosDetailRow first = rows.get(0);
		List<String> imageKeys = rows.stream()
			.map(SosDetailRow::getImageKey)
			.filter(k -> k != null)
			.toList();

		return SosDetailResponse.builder()
			.sosId(first.getSosId())
			.businessName(first.getBusinessName())
			.badge(first.getBadge())
			.businessAddr(first.getBusinessAddr())
			.businessAddrDetail(first.getBusinessAddrDetail())
			.sosTitle(first.getSosTitle())
			.sosType(first.getSosType())
			.sosContent(first.getSosContent())
			.expiresAt(first.getExpiresAt())
			.imageKeys(imageKeys)
			.build();
	}



}