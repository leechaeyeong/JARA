package com.ssafy.jara.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.ReportDao;
import com.ssafy.jara.dto.Report;

@Service
public class ReportServiceImpl implements ReportService {
	@Autowired
	ReportDao reportDao;

	@Override
	public int insertReport(Report report) {
		return reportDao.insertReport(report);
	}
	
	@Override
	public int findAccusedId(String nickname) {
		return reportDao.findAccusedId(nickname);
	}

	@Override
	public List<Report> selectListReport() {
		return reportDao.selectListReport();
	}
	
	@Override
	public String findNickname(int reporter_id) {
		return reportDao.findNickname(reporter_id);
	}
}
