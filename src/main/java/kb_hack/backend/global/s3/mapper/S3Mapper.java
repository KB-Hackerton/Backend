package kb_hack.backend.global.s3.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface S3Mapper {
    int insertProfileImageByUser(@Param("storageKey") String storageKey, @Param("memberId") Long memberId);
    int insertDefaultProfileImage(Long memberId);
}
