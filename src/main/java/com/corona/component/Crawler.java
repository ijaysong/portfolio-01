package com.corona.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.corona.domain.WorldDailyReport;
import com.corona.service.CoronaService;

/**
 * Crawler
 * @EnableScheduling를 활성화하여 스케쥴링에 사용할 클래스
 */
@Component
public class Crawler {
	
	/** 존스홉킨스 코로나 CSV URL **/
	private static final String CONN_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/01-22-2020.csv";
	
	private static final int COUNTRY_FIELD = 1;
	
	private static final int CONFIRMED_FIELD = 3;
	
	private static final int DEATHS_FIELD = 4;
	
	private static final int RECOVERED_FIELD = 5;
	
	@Autowired
	CoronaService service;

    @Scheduled(cron = "0 0 9 * * *")
    public void cronJobSchedule() {
        // 매일 9시에 CronJob을 실행한다
    	List<WorldDailyReport> info = execCrawling();
    	insertCsvData(info);
    }
    
    public List<WorldDailyReport> execCrawling() {
    	List<WorldDailyReport> result = new ArrayList<>();
    	// 1. HTML 가져오기 
    	try {
			
    		URL u = new URL(CONN_URL);
    		HttpURLConnection http = (HttpURLConnection)u.openConnection();
    		http.setRequestMethod("GET");
    		
    		if (200 > http.getResponseCode() && http.getResponseCode() > 299) {
    			return result;
    		} 
    		
    		BufferedReader br = new BufferedReader(new InputStreamReader(http.getInputStream()));
    		br.readLine(); // 한 줄 스킵함
    		
    		String line;
    		while((line = br.readLine() ) != null){
    			String[] splitted = line.split(",", 6);
    			
    			int confirmed = "".equals(splitted[CONFIRMED_FIELD]) ? 0 : Integer.parseInt(splitted[CONFIRMED_FIELD]);
    			int deaths = "".equals(splitted[DEATHS_FIELD]) ? 0 : Integer.parseInt(splitted[DEATHS_FIELD]);
    			int recovered = "".equals(splitted[RECOVERED_FIELD]) ? 0 : Integer.parseInt(splitted[RECOVERED_FIELD]);
    			
    			WorldDailyReport c = new WorldDailyReport();
    			c.setCountry(splitted[COUNTRY_FIELD]);
    			c.setConfirmed(confirmed);
    			c.setDeaths(deaths);
    			c.setRecovered(recovered);
    			
    			result.add(c);
    		}

		} catch (IOException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    public void insertCsvData(List<WorldDailyReport> info) {
    	service.insertFile(info);
    }
}
