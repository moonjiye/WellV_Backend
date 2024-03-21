package com.HealthCare.HealthyLife_Backend.controller.medicine;

import com.HealthCare.HealthyLife_Backend.dto.medicine.ElasticsearchDto;
import com.HealthCare.HealthyLife_Backend.service.medicine.ElasticsearchFilterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/filter")
@ConditionalOnProperty(name = "spring.elasticsearch.enabled", havingValue = "true")
public class ElasticsearchFilterController {


    private final ElasticsearchFilterService elasticsearchFilterService;

    public ElasticsearchFilterController(ElasticsearchFilterService elasticsearchFilterService) {
        this.elasticsearchFilterService = elasticsearchFilterService;
    }

    @GetMapping("/find")
    public ResponseEntity<?> findBySize(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<ElasticsearchDto> results = elasticsearchFilterService.findBySize(page, size);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("데이터 조회 실패: " + e.getMessage());
        }
    }

    @GetMapping("/total-count")
    public ResponseEntity<?> getTotalCount() {
        try {
            Long totalCount = elasticsearchFilterService.getTotalCount();
            return ResponseEntity.ok(totalCount);
        } catch (Exception e) {
            log.error("Error during getTotalCount: ", e);
            return ResponseEntity.badRequest().body("총 문서수 조회 실패" + e.getMessage());
        }
    }
    // 검색조건 다중 적용된것 , 현재 테스트 중
    @GetMapping("/search")
    public ResponseEntity<?> search(
            @RequestParam(required = false) String query,
            @RequestParam(required = false , defaultValue = "통합") String filter,
            @RequestParam(required = false) String functionalities,
            // type은 domestic, foreign
            @RequestParam(required = false) String originType,
            @RequestParam(required = false) String sortField,
            @RequestParam(defaultValue = "true") boolean sortAscending,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<ElasticsearchDto> results = elasticsearchFilterService.findByFilter(
                    query, filter, functionalities, originType,
                    sortField, sortAscending, page, size);
            System.out.println(results);
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            log.error("Error during search: ", e);
            return ResponseEntity.badRequest().body("검색 실패: " + e.getMessage());
        }
    }
}
