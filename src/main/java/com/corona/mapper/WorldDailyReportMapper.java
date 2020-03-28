package com.corona.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.corona.domain.WorldDailyReport;

@Mapper
public interface WorldDailyReportMapper {
	// 코로나 정보 취득
	public List<WorldDailyReport> getWorldList();
	// 코로나 정보 등록
	public void insertWorldList(List<WorldDailyReport> info);
	// 특정 국가의 코로나 정보 취득
	public List<WorldDailyReport> getDetailList(String country);
	// 국가 정보 취득
	public List<String> getCountryList();
	// 국가 정보 등록
	public void addCountryList(List<String> countryList);
}
