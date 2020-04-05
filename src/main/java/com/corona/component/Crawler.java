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
	
	/** 현재날짜 **/
	private static String current_datetime = "";
	
	/** 존스홉킨스 코로나 CSV URL **/
	private static final String CONN_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports/";
	
	/** 존스홉킨스 코로나 CSV파일 확장자 **/
	private static final String EXTENSION = ".csv";
	
	/** 국가 필드 위치 **/
	private static int COUNTRY_FIELD;
	
	/** 확진환자 필드 위치 **/
	private static int CONFIRMED_FIELD;
	
	/** 사망자 필드 위치 **/
	private static int DEATHS_FIELD;
	
	/** 격리해제 필드 위치 **/
	private static int RECOVERED_FIELD;
	
	
	@Autowired
	WorldDailyReportService service;

	
	/**
	 * 크롤링 작업
	 * 매일 오전 9시에 CronJob을 실행하는 메소드
	 */
    @Scheduled(cron = "0 30 9 * * *")
    public void cronJobSchedule() {
    	logger.info("Execute Crawler");
    	
    	// 오늘의 날짜를 취득한다
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    	LocalDate today = LocalDate.now().minusDays(1);
    	current_datetime = formatter.format(today).toString();
    	
    	// 코로나 CSV파일 내용을 읽어들인다
    	List<WorldDailyReport> info = execCrawling();
    	// 국가 정보를 등록한다
    	insertCountry(info);
    	// 코로나 정보를 등록한다
    	insertCsvData(info);
    }
    
    
	/**
	 * 크롤링 실행
	 * 존스홉킨스 의대의 코로나 CSV 데이터를 읽어들인다
	 * 
	 * @return 코로나 CSV 데이터
	 */
	public List<WorldDailyReport> execCrawling() {
    	List<WorldDailyReport> result = new ArrayList<>();
    	
    	Map<String, Integer> confirmedMap = new HashMap<>();
    	Map<String, Integer> deathsMap = new HashMap<>();
    	Map<String, Integer> recoveredMap = new HashMap<>();
    	
    	CSVReader reader = null;
    	try {
    		// URL과 연결한다
    		URL u = new URL(CONN_URL+current_datetime+EXTENSION);
    		HttpURLConnection http = (HttpURLConnection)u.openConnection();
    		http.setRequestMethod("GET");
    		
    		// Response상태를 체크한다
    		if (200 > http.getResponseCode() && http.getResponseCode() > 299) {
    			logger.warn("Response 상태가 200번대가 아님");
    			return result;
    		} 
    		// CSV파일의 내용을 읽어들인다 
    		reader = new CSVReader(new InputStreamReader(http.getInputStream()));
    		String[] header = reader.readNext();
    		
    		// CSV파일의 필드정보를 읽어들인다
    		for(int i = 0; i < header.length; i++) {
    			switch(header[i]){
	    			case "Country/Region": // 국가
	    				COUNTRY_FIELD = i;
	    				break;
	    			case "Country_Region": // 국가
	    				COUNTRY_FIELD = i;
	    				break;
	    			case "Confirmed": // 확진환자
	    				CONFIRMED_FIELD = i;
	    				break;
	    			case "Deaths": // 사망자
	    				DEATHS_FIELD = i;
	    				break;
	    			case "Recovered": // 격리해제
	    				RECOVERED_FIELD = i;
	    				break;
	    			default:
	    				break;
    			}
    		}
    		
    		// 한줄씩 읽으면서 각 필드의 정보를 취득한다
    		String[] line;
    		while((line = reader.readNext()) != null){
    			
    			String country = line[COUNTRY_FIELD];
    			int confirmed = "".equals(line[CONFIRMED_FIELD]) ? 0 : Integer.parseInt(line[CONFIRMED_FIELD]);
    			int deaths = "".equals(line[DEATHS_FIELD]) ? 0 : Integer.parseInt(line[DEATHS_FIELD]);
    			int recovered = "".equals(line[RECOVERED_FIELD]) ? 0 : Integer.parseInt(line[RECOVERED_FIELD]);
    			
    			if(confirmedMap.containsKey(country)) {
    				// 동일 국가의 내용이 이미 존재했다면
    				confirmedMap.put(country, confirmedMap.get(country)+confirmed);
    				deathsMap.put(country, deathsMap.get(country)+deaths);
    				recoveredMap.put(country, recoveredMap.get(country)+recovered);
    			} else {
    				// 동일 국가의 내용이 존재하지 않았다면
    				confirmedMap.put(country, confirmed);
    				deathsMap.put(country, deaths);
    				recoveredMap.put(country, recovered);
    			}
    		}
    		
    		// 각 국가의 정보를 한데 모은다 (확진자, 사망자, 격리해제)
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
    
	
	/**
	 * 코로나 정보 등록, 국가 정보 등록
	 * 코로나 정보에 국가 번호를 지정하여 DB에 등록한다
	 * 
	 * @param 코로나 CSV 데이터
	 */
    public void insertCsvData(List<WorldDailyReport> info) {
    	// 코로나 정보 테이블에 등록할 데이터
    	List<WorldDailyReport> coronaInfo = new ArrayList<>();
    	// DB에 이미 등록되어 있는 국가정보를 가져온다 (국가번호, 국가명)
    	List<String> countryList = service.getCountryList();
    	
    	if(info.size() > 0 && countryList.size() > 0) {
    		
    		for(WorldDailyReport target : info) {
    			String country = target.getCountryName();
    			
    			for(int i = 0; i < countryList.size(); i++) {
    				// 테이블에 해당 국가의 정보가 이미 존재한다면, 국가번호를 지정해준다
    				if(country.equals(countryList.get(i))) target.setCountryId(i+1);
    			}
    			coronaInfo.add(target);
    		}
    		
    		// 국가번호를 지정한 코로나 정보를 등록한다 
    		if(coronaInfo.size() > 0) {
    			service.insertWorldList(coronaInfo);
    		}
    	} else {
    		logger.warn("코로나 CSV 데이터 혹은 국가 정보가 없음");
    	}
    	
    }
    
    
	/**
	 * 국가 정보 등록
	 * 테이블에 존재하지 않는 국가정보를 등록한다 
	 * 
	 * @param 코로나 CSV 데이터
	 */
    private void insertCountry(List<WorldDailyReport> info) {
    	// 국가 정보 테이블에 등록할 데이터
    	List<String> countryInfo = new ArrayList<>();
    	// DB에 이미 등록되어 있는 국가정보를 가져온다 (국가번호, 국가명)
    	List<String> countryList = service.getCountryList();
    	
    	if(countryList.size() > 0) {
    		
    		Set<String> set = new HashSet<>(countryList);
    		
    		for(WorldDailyReport row : info) {
    			String country = row.getCountryName();
    			
    			if(!set.contains(country)) {
    				// 테이블에 해당 국가명이 존재하지 않는다면, 등록한다
    				countryInfo.add(country);
    			}
    		}
    		
    		if(countryInfo.size() > 0) {
    			service.addCountryList(countryInfo);
    		}
    	} 
	}
}
