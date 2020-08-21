package com.ssafy.jara.service;

import java.util.List;

import com.ssafy.jara.dto.Report;

public interface ReportService {
	
	// 신고하기
	public int insertReport(Report report);

	// 피신고자 닉네임으로 신고 리스트 찾기
	public int findAccusedId(String nickname);

	// 신고 리스트 조회하기
	public List<Report> selectListReport();
	
	// 신고자 아이디로 신고자 닉네임 찾기
	public String findNickname(int reporter_id);
}
