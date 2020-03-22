package com.corona.service;

import java.util.List;

import com.corona.domain.WorldDailyReport;

public interface WorldDailyReportService {
	public List<WorldDailyReport> getWorldList();
	public void insertWorldList(List<WorldDailyReport> info);
}
