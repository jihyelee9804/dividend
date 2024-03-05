package com.jihye.dividend.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
public class CompanyController {

    // 배당금 검색 - 자동 완성
    @GetMapping("/autocomplete")
    public ResponseEntity<?> autocomplete(@RequestParam String keyword) {
        return null;
    }

    // 회사 리스트 조회
    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    // 배당금 데이터 저장
    @PostMapping
    public ResponseEntity<?> addCompany() {
        return null;
    }

    // 배당금 데이터 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
