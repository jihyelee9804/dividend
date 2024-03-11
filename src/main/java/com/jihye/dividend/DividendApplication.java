package com.jihye.dividend;

import com.jihye.dividend.model.Company;
import com.jihye.dividend.model.ScrapedResult;
import com.jihye.dividend.scraper.YahooFinaceScraper;
import org.apache.commons.collections4.Trie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class DividendApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DividendApplication.class, args);
    }
}
