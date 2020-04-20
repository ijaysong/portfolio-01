package com.corona.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corona.domain.WorldDailyReport;
import com.corona.mapper.WorldDailyReportMapper;

@Service
public class WorldDailyReportServiceImpl implements WorldDailyReportService {
	
	@Autowired
	private WorldDailyReportMapper mapper;

	@Override
	public List<WorldDailyReport> getWorldList() throws Exception {
		return mapper.getWorldList();
	}

	@Override
	public void insertWorldList(List<WorldDailyReport> info) throws Exception {
		mapper.insertWorldList(info);
	}

	@Override
	public List<WorldDailyReport> getDetailList(String country) throws Exception {
		return mapper.getDetailList(country);
	}

	@Override
	public List<String> getCountryList() throws Exception {
		return mapper.getCountryList();
	}

	@Override
	public void addCountryList(List<String> countryList) throws Exception {
		mapper.addCountryList(countryList);
	}

	@Override
	public boolean checkCrawlingExecution(String today) throws Exception {
		return mapper.checkCrawlingExecution(today);
	}

	@Override
	public void updateCrawlingDate(String today) throws Exception {
		mapper.updateCrawlingDate(today);
	}

}
