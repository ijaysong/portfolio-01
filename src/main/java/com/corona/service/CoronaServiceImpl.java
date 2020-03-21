package com.corona.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.corona.domain.WorldDailyReport;
import com.corona.mapper.CoronaMapper;

@Service
public class CoronaServiceImpl implements CoronaService {
	
	@Autowired
	private CoronaMapper mapper;

	@Override
	public List<WorldDailyReport> getWorldList() {
		return mapper.getWorldList();
		
	}

	@Override
	public void insertData(List<WorldDailyReport> info) {
		return mapper.insertData(info);
	}

}
