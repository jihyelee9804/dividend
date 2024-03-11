package com.jihye.dividend.service;

import com.jihye.dividend.exception.impl.NoCompanyException;
import com.jihye.dividend.model.Company;
import com.jihye.dividend.model.Dividend;
import com.jihye.dividend.model.ScrapedResult;
import com.jihye.dividend.persist.CompanyRepository;
import com.jihye.dividend.persist.DividendRepository;
import com.jihye.dividend.persist.entity.CompanyEntity;
import com.jihye.dividend.persist.entity.DividendEntity;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FinanceService {
    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    @Cacheable(key = "companyName", value = "finance")
    public ScrapedResult getDividendByCompanyName(String companyName) {
        // 1. 회사명을 기준으로 회사 정보를 조회
        CompanyEntity company = this.companyRepository.findByName(companyName)
                .orElseThrow(() -> new NoCompanyException());
        // 2. 조회된 회사 ID로 배당금 정보 조회
        List<DividendEntity> dividendEntities = this.dividendRepository.findAllByCompanyId(company.getId());

        // 3. 결과 조립 후 반환

        List<Dividend> dividends = dividendEntities.stream()
                                                    .map(e -> new Dividend(e.getDate(), e.getDividend()))
                                                    .collect(Collectors.toList());
        return new ScrapedResult(new Company(company.getTicker(), company.getName()),
                                        dividends);
    }
}
