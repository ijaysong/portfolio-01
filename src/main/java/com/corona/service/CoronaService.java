package com.corona.service;

import java.util.List;

import com.corona.domain.WorldDailyReport;

public interface CoronaService {
	public List<WorldDailyReport> getWorldList();
	public void insertData(List<WorldDailyReport> info);
}
