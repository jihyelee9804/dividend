package com.jihye.dividend.scraper;

import com.jihye.dividend.constants.Month;
import com.jihye.dividend.model.Company;
import com.jihye.dividend.model.Dividend;
import com.jihye.dividend.model.ScrapedResult;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 회사 메타정보 및 배당금 정보를 스크래핑하는 클래스
 * @return : ScrapedResult (멤버변수: 회사명, 배당금 정보 리스트)
 */
@Component // YahooFinanceScraper를 빈으로 등록한다.
public class YahooFinaceScraper implements Scraper{
    // Yahoo Finance 사이트 url을 상수로 저장한다. 문자열 포맷스트링값은 company, startTime, endTime (배당금 정보 관련 날짜값)
    private static final String STATISTICS_URL = "https://finance.yahoo.com/quote/%s/history?period1=%d&period2=%d&interval=1mo";
    private static final String SUMMARY_URL = "https://finance.yahoo.com/quote/%s?p=%s";
    private static final long START_TIME = 86400; // 60 * 60 * 24, 시작날짜 고정값

    // 회사의 배당금 정보를 스크래핑
    @Override
    public ScrapedResult scrap(Company company) {
        // 최종적으로 리턴할 ScrapedResult 객체 생성
        ScrapedResult scrapResult = new ScrapedResult();
        // 회사명을 set한다.
        scrapResult.setCompany(company);
        // Yahoo Finance 사이트에서 배당금 정보 parsing 및 저장하는 과정
        try {
            // 밀리초 단위의 현재시간을 초 단위로 변환한다.
            long now = System.currentTimeMillis() / 1000;
            String url = String.format(STATISTICS_URL, company.getName(), START_TIME, now);
            // url 과 연결한다.
            Connection connection = Jsoup.connect(url);
            Document document = null;
            try {
                document = connection.get(); //html 문서가 반환된다.
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            // html 문서를 parsing한 내용을 반환한다. parsing된 내용은 div들로 구성되어 있다.
            Elements parsingDivs = document.getElementsByAttributeValue("data-test", "historical-prices");
            // parsingDivs의 첫 번째 요소인 table을 저장한다.
            Element tableEle = parsingDivs.get(0); // 테이블 전체
            // tbody
            Element tbody = tableEle.children().get(1);
            // 배당금 정보를 담을 리스트 생성
            List<Dividend> dividends = new ArrayList<>();
            // tbody 요소들을 순회하면서 배당금과 날짜 정보를 저장한다.
            for (Element e : tbody.children()) {
                String txt = e.text();
                // 배당금 정보가 없는 요소면 continue한다.
                if (!txt.endsWith("Dividend")) {
                    continue;
                }

                String[] splits = txt.split(" ");
                int month = Month.strToNumber(splits[0]);
                int day = Integer.valueOf(splits[1].replace(",", ""));
                int year = Integer.valueOf(splits[2]);
                String dividend = splits[3];
                // month가 0보다 작은 값이면 예외를 던진다.
                if (month < 0) {
                    throw new RuntimeException("Unexpected Month enum value ->" + splits[0]);
                }
                // dividends 리스트에 요소 추가
                dividends.add(Dividend.builder()
                        .date(LocalDateTime.of(year, month, day, 0, 0))
                        .dividend(dividend)
                        .build()
                );
            }
            // 배당금 정보 리스트를 set한다.
            scrapResult.setDividendEntities(dividends);
        } catch(Exception e) {
            e.printStackTrace();
        }
    return scrapResult;
    }

    // 회사의 메타정보를 스크래핑
    @Override
    public Company scrapCompanyByTicker(String ticker) {
        String url = String.format(SUMMARY_URL, ticker, ticker);
        try {
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Element titleEle = document.getElementsByTag("h1").get(0);
            // 문자열 후처리
            String title = titleEle.text().split(" - ")[1].trim();
            return Company.builder()
                    .ticker(ticker)
                    .name(title)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ticker에 해당하는 Company가 없으면 null을 반환한다.
        return null;
    }
}
