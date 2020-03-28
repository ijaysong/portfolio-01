package com.corona.component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.corona.domain.WorldDailyReport;
import com.corona.service.WorldDailyReportService;
import com.opencsv.CSVReader;

/**
 * Crawler
 * @EnableScheduling를 활성화하여 스케쥴링에 사용할 클래스
 */
@Component
public class Crawler {
	
	private static Logger logger = LoggerFactory.getLogger(Crawler.class);
	
	private static String current_datetime = "";
	
	/** 존스홉킨스 코로나 CSV URL **/
	private static final String CONN_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
	
	private static final String EXTENSION = ".csv";
	
	private static int COUNTRY_FIELD;
	
	private static int CONFIRMED_FIELD;
	
	private static int DEATHS_FIELD;
	
	private static int RECOVERED_FIELD;
	
	
	@Autowired
	WorldDailyReportService service;

	// 매일 9시에 CronJob을 실행한다
    @Scheduled(cron = "0 0 9 * * *")
    public void cronJobSchedule() {
    	logger.info("Execute Crawler");
    	
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    	LocalDate today = LocalDate.now().minusDays(1);
    	current_datetime = formatter.format(today).toString();
    	
    	List<WorldDailyReport> info = execCrawling();
    	insertCountry(info);
    	insertCsvData(info);
    }
    
	public List<WorldDailyReport> execCrawling() {
    	List<WorldDailyReport> result = new ArrayList<>();
    	
    	Map<String, Integer> confirmedMap = new HashMap<>();
    	Map<String, Integer> deathsMap = new HashMap<>();
    	Map<String, Integer> recoveredMap = new HashMap<>();
    	
    	CSVReader reader = null;
    	// 1. HTML 가져오기 
    	try {
    		URL u = new URL(CONN_URL+current_datetime+EXTENSION);
    		HttpURLConnection http = (HttpURLConnection)u.openConnection();
    		http.setRequestMethod("GET");
    		
    		if (200 > http.getResponseCode() && http.getResponseCode() > 299) {
    			return result;
    		} 
    		reader = new CSVReader(new InputStreamReader(http.getInputStream()));
    		String[] header = reader.readNext();
    		
    		for(int i = 0; i < header.length; i++) {
    			switch(header[i]){
	    			case "Country/Region":
	    				COUNTRY_FIELD = i;
	    				break;
	    			case "Country_Region":
	    				COUNTRY_FIELD = i;
	    				break;
	    			case "Confirmed":
	    				CONFIRMED_FIELD = i;
	    				break;
	    			case "Deaths":
	    				DEATHS_FIELD = i;
	    				break;
	    			case "Recovered":
	    				RECOVERED_FIELD = i;
	    				break;
	    			default:
	    				break;
    			}
    		}
    		
    		String[] line;
    		while((line = reader.readNext()) != null){
    			
    			String country = line[COUNTRY_FIELD];
    			int confirmed = "".equals(line[CONFIRMED_FIELD]) ? 0 : Integer.parseInt(line[CONFIRMED_FIELD]);
    			int deaths = "".equals(line[DEATHS_FIELD]) ? 0 : Integer.parseInt(line[DEATHS_FIELD]);
    			int recovered = "".equals(line[RECOVERED_FIELD]) ? 0 : Integer.parseInt(line[RECOVERED_FIELD]);
    			
    			if(confirmedMap.containsKey(country)) {
    				confirmedMap.put(country, confirmedMap.get(country)+confirmed);
    				deathsMap.put(country, deathsMap.get(country)+deaths);
    				recoveredMap.put(country, recoveredMap.get(country)+recovered);
    			} else {
    				confirmedMap.put(country, confirmed);
    				deathsMap.put(country, deaths);
    				recoveredMap.put(country, recovered);
    			}
    		}
    		
    		for(String key : confirmedMap.keySet()) {
    			WorldDailyReport report = new WorldDailyReport();
    			report.setCountryName(key);
    			report.setConfirmed(confirmedMap.get(key));
    			report.setDeaths(deathsMap.get(key));
    			report.setRecovered(recoveredMap.get(key));
    			
    			result.add(report);
    		}
    		
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return result;
    }
    
    public void insertCsvData(List<WorldDailyReport> info) {
    	List<String> countryList = service.getCountryList();
    	List<WorldDailyReport> insertList = new ArrayList<>();
    	
    	for(WorldDailyReport target : info) {
    		String country = target.getCountryName();
    		
    		for(int i = 0; i < countryList.size(); i++) {
    			if(country.equals(countryList.get(i))) target.setCountryId(i+1);
    		}
    		insertList.add(target);
    	}
    	
    	service.insertWorldList(insertList);
    }
    
    private void insertCountry(List<WorldDailyReport> info) {
    	List<String> insertList = new ArrayList<>();
    	List<String> countryList = service.getCountryList();
    	
    	if(countryList.size() > 0) {
    		
    		Set<String> countrySet = new HashSet<>(countryList);
    		
    		for(WorldDailyReport target : info) {
    			String country = target.getCountryName();
    			
    			if(!countrySet.contains(country)) {
    				// Country 테이블에 해당 국가명이 존재하지 않는다면,
    				insertList.add(country);
    			}
    		}
    		
    		if(insertList.size() > 0) {
    			service.addCountryList(insertList);
    		}
    	}
	}
}
