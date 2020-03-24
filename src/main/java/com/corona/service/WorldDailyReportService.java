package com.corona.service;

import java.util.List;

import com.corona.domain.WorldDailyReport;

public interface WorldDailyReportService {
	public List<WorldDailyReport> getWorldList();
	public void insertWorldList(List<WorldDailyReport> info);
	public List<WorldDailyReport> getDetailList(String country);
	public List<String> getCountryList();
	public void addCountryList(List<String> countryList);
}
