package com.ssafy.jara.controller;

import java.util.List;

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

import com.ssafy.jara.dto.BarterComment;
import com.ssafy.jara.service.BarterCommentService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/barters")
public class BarterCommentController {
	
	@Autowired
	BarterCommentService barterCommentService;
	
	@ApiOperation(value = "물물교환 새로운 댓글 등록", response = String.class)
	@PostMapping("/{item_id}/comments")
	private ResponseEntity<BarterComment> insertBarterComment(@PathVariable int item_id, @RequestBody BarterComment barterComment) {
		if (barterCommentService.insertBarterComment(barterComment) > 0) {
			BarterComment newBarterComment = barterCommentService.selectBarterComment(barterComment.getId());
			return new ResponseEntity<BarterComment>(newBarterComment, HttpStatus.OK);
		} else {
			return new ResponseEntity<BarterComment>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "물물교환 댓글 수정 (contents)", response = String.class)
	@PutMapping("/{item_id}/comments/{id}")
	private ResponseEntity<BarterComment> updateBarterComment(@PathVariable int id, @RequestBody BarterComment barterComment) {
		if (barterCommentService.updateBarterComment(barterComment) > 0) {
			BarterComment updatedBarterComment = barterCommentService.selectBarterComment(id);
			return new ResponseEntity<BarterComment>(updatedBarterComment, HttpStatus.OK);
		} else {
			return new ResponseEntity<BarterComment>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "물물교환 댓글 삭제", response = String.class)
	@DeleteMapping("/{item_id}/comments/{id}")
	private ResponseEntity<String> deleteBarterComment(@PathVariable int id) {
		if (barterCommentService.deleteBarterComment(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
		}
	}
	
	@ApiOperation(value = "물물교환 전체 댓글 조회", response = String.class)
	@GetMapping("/{item_id}/comments")
	private ResponseEntity<List<BarterComment>> selectListBarterComment(@PathVariable String item_id) {
		return new ResponseEntity<List<BarterComment>>(barterCommentService.selectListBarterComment(Integer.parseInt(item_id)), HttpStatus.OK);
	}
}
