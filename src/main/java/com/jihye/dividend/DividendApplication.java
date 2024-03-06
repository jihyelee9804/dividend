package com.jihye.dividend;

import com.jihye.dividend.model.Company;
import com.jihye.dividend.model.ScrapedResult;
import com.jihye.dividend.scraper.YahooFinaceScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DividendApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(DividendApplication.class, args);

    }
}
