package com.ssafy.jara.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jara.common.service.jwt.JwtService;
import com.ssafy.jara.dto.Report;
import com.ssafy.jara.service.AccountService;
import com.ssafy.jara.service.ReportService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/reports")
public class ReportController {

	@Autowired
	ReportService reportService;

	@Autowired
	AccountService accountService;
	
	@Autowired
	private JwtService jwtService;

	@ApiOperation(value = "사용자 신고 등록", response = String.class)
	@PostMapping("")
	private ResponseEntity<String> insertReport(@RequestBody Report report) {

		if (reportService.insertReport(report) > 0) { // 신고테이블에 추가
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);

	}
	
	@ApiOperation(value = "관리자 페이지 - 신고 리스트", response = String.class)
	@GetMapping("admin")
	private ResponseEntity<Map<String, Object>> selectListReport(@RequestHeader(value="token") String token) {
		Map<String, Object> tokenMap = new HashMap<>();
		Map<String, Object> accountMap = new HashMap<>();
		
		tokenMap.putAll(jwtService.get(token));
		accountMap = (Map<String, Object>) tokenMap.get("Account");
		String nickname = (String) accountMap.get("nickname");
		
		Map<String, Object> resultMap = new HashMap<>();
		
		if (nickname.equals("관리자")) { // 관리자만 신고 리스트 관리 가능
			List<Report> report = reportService.selectListReport();
			
			resultMap.put("report", report);
			resultMap.put("isAdmin", true);
			if (!reportService.selectListReport().equals(null)) {
				return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
			} else {
				return new ResponseEntity<Map<String, Object>>(HttpStatus.NO_CONTENT);
			}
		} else {
			resultMap.put("isAdmin", false);
			return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
		}
	}
	
	@ApiOperation(value = "관리자 페이지 - 계정 삭제", response = String.class)
	@DeleteMapping("admin")
	private ResponseEntity<List<Report>> deleteAccount(@RequestParam("accused_nickname") String accused_nickname, @RequestHeader(value="token") String token) {
		Map<String, Object> tokenMap = new HashMap<>();
		Map<String, Object> accountMap = new HashMap<>();
		
		tokenMap.putAll(jwtService.get(token));
		accountMap = (Map<String, Object>) tokenMap.get("Account");
		String nickname = (String) accountMap.get("nickname");

		if (nickname.equals("관리자")) {
			// int id = reportService.findAccusedId(report.getAccused_nickname());
			int id = reportService.findAccusedId(accused_nickname);
					
			if(accountService.deleteAccount(id) > 0) { // 회원 삭제
			
				return new ResponseEntity<List<Report>>(reportService.selectListReport(), HttpStatus.OK);
			} else {
				return new ResponseEntity<List<Report>>(HttpStatus.BAD_REQUEST);
			}
		} else {
			return new ResponseEntity<List<Report>>(HttpStatus.FORBIDDEN);
		}
	}
}
