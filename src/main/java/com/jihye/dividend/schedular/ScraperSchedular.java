package com.jihye.dividend.schedular;

import com.jihye.dividend.model.Company;
import com.jihye.dividend.model.ScrapedResult;
import com.jihye.dividend.persist.CompanyRepository;
import com.jihye.dividend.persist.DividendRepository;
import com.jihye.dividend.persist.entity.CompanyEntity;
import com.jihye.dividend.persist.entity.DividendEntity;
import com.jihye.dividend.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
public class ScraperSchedular {

    private final CompanyRepository companyRepository;
    private final Scraper YahooFinaceScraper;
    private final DividendRepository dividendRepository;

//    @Scheduled(fixedDelay = 1000)
//    public void test1() throws InterruptedException {
//        Thread.sleep(10000);
//        System.out.println(Thread.currentThread().getName() + " ->  테스트 1 : " + LocalDateTime.now());
//    }
//
//    @Scheduled(fixedDelay = 1000)
//    public void test2(){
//        System.out.println(Thread.currentThread().getName() + " ->  테스트 2 : " + LocalDateTime.now());
//    }


//    @Scheduled(cron = "${schedular.scrap.yahoo}") // 매일 12시에 실행된다.
    public void  yahooFinanceScheduling() {
        log.info("scrapping schedular is started");

        // 저장된 회사 목록을 조회
        List<CompanyEntity> companies = this.companyRepository.findAll();
        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companies) {
            log.info("scrapping schedular is started" + company.getName());
            ScrapedResult scrapedResult = this.YahooFinaceScraper.scrap(new Company(company.getName(), company.getTicker()));
            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividendEntities().stream()
                    // 배당금 모델을 배당금 엔티티로 매핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    // 엘리먼트를 하나씩 배당금 레파지토리에 삽입
                    .forEach(e -> {
                        boolean exists = this.dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        // 회사명과 날짜가 중복되지 않는 데이터면 저장한다.
                        if (!exists) {
                            this.dividendRepository.save(e);
                            log.info("insert new dividend -> " + e.toString());
                        }
                    });
            // 연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 일시정지
            try {
                Thread.sleep(3000); // 3초동안 일시정지한다.
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        

    }
}
