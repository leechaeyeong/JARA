package com.ssafy.jara.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

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

import com.ssafy.jara.dto.EitherComment;
import com.ssafy.jara.service.EitherCommentService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/eithers")
public class EitherCommentController {
	
	@Autowired
	EitherCommentService eitherCommentService;
	
	@ApiOperation(value = "투표 새로운 댓글 등록", response = String.class)
	@PostMapping("/{either_id}/comments")
	private ResponseEntity<EitherComment> insertEitherComment(@PathVariable int either_id, @RequestBody EitherComment eitherComment) {
		int ret = eitherCommentService.insertEitherComment(eitherComment);
		if (ret > 0) {
			return new ResponseEntity<EitherComment>(eitherCommentService.selectEitherComment(eitherComment.getId()), HttpStatus.OK);
		} else {
			return new ResponseEntity<EitherComment>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "투표 댓글 수정 (contents)", response = String.class)
	@PutMapping("/{either_id}/comments/{id}")
	private ResponseEntity<HashMap<String, Object>> updateEitherComment(@PathVariable int id, @RequestBody EitherComment eitherComment) {
		if (eitherCommentService.updateEitherComment(eitherComment) > 0) {
			
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
			
			eitherComment = eitherCommentService.selectEitherComment(id);
			
			hashMap.put("updated_at", dateFormat.format(eitherComment.getUpdated_at()));
			hashMap.put("contents", eitherComment.getContents());
			hashMap.put("choice", eitherComment.isChoice());
			
			return new ResponseEntity<HashMap<String, Object>>(hashMap, HttpStatus.OK);
		} else {
			return new ResponseEntity<HashMap<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "투표 댓글 삭제", response = String.class)
	@DeleteMapping("/{either_id}/comments/{id}")
	private ResponseEntity<String> deleteEitherComment(@PathVariable int id) {
		if (eitherCommentService.deleteEitherComment(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "투표 전체 댓글 조회", response = String.class)
	@GetMapping("/{either_id}/comments")
	private ResponseEntity<List<EitherComment>> selectListEitherComment(@PathVariable int either_id) {
		return new ResponseEntity<List<EitherComment>>(eitherCommentService.selectListEitherComment(either_id), HttpStatus.OK);
	}
}
