package com.HealthCare.HealthyLife_Backend.service;


import com.HealthCare.HealthyLife_Backend.dto.SeasonRankingDto;
import com.HealthCare.HealthyLife_Backend.dto.TotalRankingDto;
import com.HealthCare.HealthyLife_Backend.entity.Member;
import com.HealthCare.HealthyLife_Backend.entity.SeasonRanking;
import com.HealthCare.HealthyLife_Backend.repository.MemberRepository;
import com.HealthCare.HealthyLife_Backend.repository.SeasonRankingRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SeasonRankingService {

    private final SeasonRankingRepository seasonRankingRepository;


    public SeasonRankingService(SeasonRankingRepository seasonRankingRepository) {
        this.seasonRankingRepository = seasonRankingRepository;
    }

    public List<SeasonRankingDto> getMemberPointsForCurrentMonth() {

        YearMonth currentYearMonth = YearMonth.now();
        String yearMonth = currentYearMonth.toString().replace("-", "");

        List<SeasonRanking> seasonRankings = seasonRankingRepository.findByRegDateStartingWithOrderByPointsDesc(yearMonth);
        System.out.println(seasonRankings);


        int rank = 1; // 현재 순위
        int actualRank = 1; // 실제 순위 (동점자 처리를 위해 사용)
        Integer previousPoints = null; // 이전 순위의 포인트

        for (SeasonRanking seasonRanking : seasonRankings) {
            if (previousPoints == null || !seasonRanking.getPoints().equals(previousPoints)) {
                rank = actualRank; // 포인트가 달라지면, 현재의 실제 순위를 순위 변수에 할당
                previousPoints = seasonRanking.getPoints(); // 포인트 업데이트
            }
            seasonRanking.setRanks(rank); // 순위 설정
            actualRank++; // 다음 순위로 이동
        }

        // DTO로 변환하여 반환
        return seasonRankings.stream()
                .map(seasonRanking -> SeasonRankingDto.builder()
                        .id(seasonRanking.getId())
                        .memberId(seasonRanking.getMember().getId())
                        .points(seasonRanking.getPoints())
                        .nickname(seasonRanking.getMember().getNickName())
                        .gender(seasonRanking.getMember().getGender())
                        .regDate(seasonRanking.getRegDate())
                        .ranks(seasonRanking.getRanks())
                        .build())
                .collect(Collectors.toList());
    }
}
