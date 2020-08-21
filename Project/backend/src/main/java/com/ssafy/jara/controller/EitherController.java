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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jara.dto.Either;
import com.ssafy.jara.dto.EitherChoice;
import com.ssafy.jara.dto.EitherComment;
import com.ssafy.jara.service.EitherCommentService;
import com.ssafy.jara.service.EitherService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/eithers")
public class EitherController {
	
	@Autowired
	EitherService eitherService;
	
	@Autowired
	EitherCommentService eitherCommentService;
	
	@ApiOperation(value = "새로운 투표 등록", response = String.class)
	@PostMapping("")
	private ResponseEntity<String> insertEither(@RequestBody Either either) {
		if (eitherService.insertEither(either) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "해당 투표 조회", response = String.class)
	@GetMapping("/{id}")
	private ResponseEntity<Map<String, Object>> selectEither(@PathVariable int id) {
		Either either = eitherService.selectEither(id);
		
		if (either == null) {
			return new ResponseEntity<Map<String, Object>>(HttpStatus.NOT_FOUND);
		}
		
		List<EitherComment> eitherComments = eitherCommentService.selectListEitherComment(id);
		List<Integer> choiceA = eitherService.selectChoiceAList(id);
		List<Integer> choiceB = eitherService.selectChoiceBList(id);
		
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("either", either);
		resultMap.put("choiceA", choiceA);
		resultMap.put("choiceB", choiceB);
		resultMap.put("eitherComments", eitherComments);
		
		return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation(value = "투표 수정", response = String.class)
	@PutMapping("/{id}")
	private ResponseEntity<String> updateEither(@PathVariable int id) {
		if (eitherService.updateEither(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "투표 삭제", response = String.class)
	@DeleteMapping("/{id}")
	private ResponseEntity<String> deleteEither(@PathVariable int id) {
		if (eitherService.deleteEither(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "전체 투표 조회", response = String.class)
	@GetMapping("")
	private ResponseEntity<List<Either>> selectListEither() {
		return new ResponseEntity<List<Either>>(eitherService.selectListEither(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "투표 리스트에서 인덱스 {s_idx}번 부터 {count}개의 투표 조회", response = String.class)
	@GetMapping("/{s_idx}/{count}")
	private ResponseEntity<List<Either>> selectPartialListEither(@PathVariable int s_idx, @PathVariable int count) {
		List<Either> partialList = eitherService.selectPartialListEither(s_idx, count);
		
		if (partialList.size() > 0) {
			return new ResponseEntity<List<Either>>(partialList, HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Either>>(partialList, HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "선택지 투표", response = String.class)
	@PostMapping("/{either_id}/pick")
	private ResponseEntity<String> insertPickEither(@PathVariable int either_id, @RequestBody EitherChoice eitherChoice) {
		if (eitherService.pickEither(eitherChoice) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "이더 Top 3 조회", response = String.class)
	@GetMapping("/top3")
	private ResponseEntity<List<Either>> selectListEitherTop3() {
		return new ResponseEntity<List<Either>>(eitherService.selectListEitherTop3(), HttpStatus.OK);
	}
}
