package com.corona.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.corona.domain.WorldDailyReport;

@Mapper
public interface WorldDailyReportMapper {
	public List<WorldDailyReport> getWorldList();
	public void insertWorldList(List<WorldDailyReport> info);
	public List<WorldDailyReport> getDetailList(String country);
	public List<String> getCountryList();
	public void addCountryList(List<String> countryList);
}
