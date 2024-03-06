package com.jihye.dividend.scraper;

import com.jihye.dividend.model.Company;
import com.jihye.dividend.model.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);
    ScrapedResult scrap(Company company);
}
