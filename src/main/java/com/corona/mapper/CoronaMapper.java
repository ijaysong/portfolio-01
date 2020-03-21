package com.corona.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.corona.domain.WorldDailyReport;

@Mapper
public interface CoronaMapper {
	public List<WorldDailyReport> getWorldList();
	public void insertData(List<WorldDailyReport> info);
}
