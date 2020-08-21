package com.ssafy.jara.controller;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jara.dto.ArticleComment;
import com.ssafy.jara.service.ArticleCommentService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/articles")
public class ArticleCommentController {
	
	@Autowired
	ArticleCommentService articleCommentService;
	
	@ApiOperation(value = "게시글 댓글 등록", response = ArticleComment.class)
	@PostMapping("/{article_id}/comments")
	private ResponseEntity<ArticleComment> insertArticleComment(@PathVariable("article_id") int article_id, @RequestBody ArticleComment articleComment) {
		articleComment.setArticle_id(article_id);
		
		if(articleCommentService.insertArticleComment(articleComment) > 0) {
			return new ResponseEntity<ArticleComment>(articleCommentService.selectArticleComment(articleComment.getId()), HttpStatus.OK);
		}
		
		return new ResponseEntity<ArticleComment>(HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ApiOperation(value = "게시글 댓글 수정", response = String.class)
	@PutMapping("/{article_id}/comments/{id}")
	private ResponseEntity<HashMap<String, Object>> updateArticleComment(@RequestBody ArticleComment articleComment, @PathVariable("id") int id) {
		if(articleCommentService.updateArticleComment(articleComment) > 0) {
			
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
			
			articleComment = articleCommentService.selectArticleComment(id);
			
			hashMap.put("updated_at", dateFormat.format(articleComment.getUpdated_at()));
			hashMap.put("contents", articleComment.getContents());

			return new ResponseEntity<HashMap<String, Object>>(hashMap, HttpStatus.OK);
		}
		
		return new ResponseEntity<HashMap<String, Object>>(HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "게시글 댓글 삭제", response = String.class)
	@DeleteMapping("/{article_id}/comments/{id}")
	private ResponseEntity<String> deleteArticleComment(@PathVariable("id") int id) {
		if(articleCommentService.deleteArticleComment(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "게시글 댓글 숨김", response = String.class)
	@PutMapping("/{article_id}/comments/{id}/invisible")
	private ResponseEntity<String> updateArticleCommentInvisible(@PathVariable("id") int id) {
		if(articleCommentService.updateArticleCommentInvisible(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
}
