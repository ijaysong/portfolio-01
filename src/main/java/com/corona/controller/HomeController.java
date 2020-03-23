package com.corona.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.corona.component.Crawler;
import com.corona.domain.WorldDailyReport;
import com.corona.service.WorldDailyReportService;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private WorldDailyReportService service;
	
	@Autowired
	private Crawler c;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Model model) {
		List<WorldDailyReport> result = service.getWorldList();
		model.addAttribute("worldList", result);
		
		model.addAttribute("updatedDate", result.get(0).getUpdatedDate());
		return "home";
	}
	
	@RequestMapping(value="/{country}", method = RequestMethod.GET)
	public String detail(@PathVariable("country")String country, Model model) {
		List<WorldDailyReport> result = service.getDetailList(country);
		model.addAttribute("worldList", result);
		return "detail";
	}
	
}
