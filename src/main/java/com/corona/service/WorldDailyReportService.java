package com.corona.service;

import java.util.List;

import com.corona.domain.WorldDailyReport;

public interface WorldDailyReportService {
	// 코로나 정보 취득
	public List<WorldDailyReport> getWorldList() throws Exception;
	// 코로나 정보 등록
	public void insertWorldList(List<WorldDailyReport> info) throws Exception;
	// 특정 국가의 코로나 정보 취득
	public List<WorldDailyReport> getDetailList(String country) throws Exception;
	// 국가 정보 취득
	public List<String> getCountryList() throws Exception;
	// 국가 정보 등록
	public void addCountryList(List<String> countryList) throws Exception;
	// 크롤링 실행 유무 확인
	public boolean checkCrawlingExecution(String today) throws Exception;
	// 크롤링 완료 날짜 갱신
	public void updateCrawlingDate(String today) throws Exception;
}
