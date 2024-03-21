package com.HealthCare.HealthyLife_Backend.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Builder
public class CommunityDto {
    private Long communityId;
    private Long memberId;
    private Long categoryId;
    private String title;
    private String content; // 사진
    private String text; // 내용
    private LocalDateTime regDate;
    private int likeCount;
    private int viewCount;
    private String categoryName;
    private String email;
    private String nickName;


}