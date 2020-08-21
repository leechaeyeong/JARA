package com.ssafy.jara.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.jara.dto.Article;
import com.ssafy.jara.service.ArticleCommentService;
import com.ssafy.jara.service.ArticleService;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/jara/articles")
public class ArticleController {
	
	@Autowired
	ArticleService articleService;
	
	@Autowired
	ArticleCommentService articleCommentService;
	
	@ApiOperation(value = "사용자가 작성한 전체 게시글 및 사용자가 팔로우하는 다른 사용자의 전체 게시글 및 댓글, 좋아요 사용자 조회", response = List.class)
	@GetMapping("")
	private ResponseEntity<List<Article>> selectListArticle(@RequestParam int user_id) {
		List<Article> articleList = articleService.selectListArticle(user_id);
		
		for (int i = 0; i < articleList.size(); i++) {
			Article article = articleList.get(i);
			
			article.setComments(articleCommentService.selectArticleComments(article.getId())); // 전체 댓글 조회
			article.setLikeAccounts(articleService.selectArticleLikeAccount(article.getId())); // 전체 좋아요 사용자 조회
		}
		
		return new ResponseEntity<List<Article>>(articleList, HttpStatus.OK);
	}
	
	@ApiOperation(value = "사용자가 작성한 전체 게시글 및 사용자가 팔로우하는 다른 사용자의 전체 게시글 s_idx부터 count만큼 조회", response = List.class)
	@GetMapping("/{s_idx}/{count}")
	private ResponseEntity<List<Article>> selectRangeListArticle(@RequestParam int user_id, @PathVariable("s_idx") int s_idx, @PathVariable("count") int count) {
		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		hashMap.put("user_id", user_id);
		hashMap.put("s_idx", s_idx);
		hashMap.put("count", count);
		
		List<Article> articleList = articleService.selectRangeListArticle(hashMap);
		
		for (int i = 0; i < articleList.size(); i++) {
			Article article = articleList.get(i);
			
			article.setComments(articleCommentService.selectArticleComments(article.getId())); // 전체 댓글 조회
			article.setLikeAccounts(articleService.selectArticleLikeAccount(article.getId())); // 전체 좋아요 사용자 조회
		}
		
		return new ResponseEntity<List<Article>>(articleList, HttpStatus.OK);
	}
	
	@ApiOperation(value = "게시글 등록", response = Integer.class)
	@PostMapping("")
	private ResponseEntity<Integer> insertArticle(@RequestBody Article article) throws IOException {
		if(articleService.insertArticle(article) > 0) {
			return new ResponseEntity<Integer>(article.getId(), HttpStatus.OK);
		}
		
		return new ResponseEntity<Integer>(0, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ApiOperation(value = "게시글 이미지 경로(firebase) 저장", response = String.class)
	@PutMapping("/{id}/img")
	private ResponseEntity<String> insertArticleImg(@RequestBody Article article) {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("id", article.getId());
		hashMap.put("img_src", article.getImg_src());
		
		if(articleService.updateArticleImg(hashMap) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "게시글 및 댓글, 좋아요 하는 사용자 조회", response = Article.class)
	@GetMapping("/{id}")
	private ResponseEntity<Article> selectArticle(@PathVariable("id") int id) {
		try {
			Article article = articleService.selectArticle(id);
			article.setComments(articleCommentService.selectArticleComments(article.getId()));
			article.setLikeAccounts(articleService.selectArticleLikeAccount(article.getId()));
			
			return new ResponseEntity<Article>(article, HttpStatus.OK);
		
		} catch (NullPointerException e) {
			e.printStackTrace();
			
			return new ResponseEntity<Article>(HttpStatus.NOT_FOUND);
		}
	}
	
	@ApiOperation(value = "게시글 수정", response = String.class)
	@PutMapping("/{id}")
	private ResponseEntity<String> updateArticle(@RequestBody Article article) {
		if(articleService.updateArticle(article) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "게시글 삭제", response = String.class)
	@DeleteMapping("/{id}")
	private ResponseEntity<String> deleteArticle(@PathVariable("id") int id) {
		if(articleService.deleteArticle(id) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("fail", HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "게시글 좋아요 등록", response = String.class)
	@PostMapping("/{id}/like")
	private ResponseEntity<String> insertArticleLike(@PathVariable("id") int article_id, @RequestParam("user_id") int user_id) {
		HashMap<String, Integer> hashMap = new HashMap<>();
		hashMap.put("article_id", article_id);
		hashMap.put("user_id", user_id);
		
		if (articleService.insertArticleLike(hashMap) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ApiOperation(value = "게시글 좋아요 삭제", response = String.class)
	@DeleteMapping("/{id}/like")
	private ResponseEntity<String> deleteArticleLike(@PathVariable("id") int article_id, @RequestParam("user_id") int user_id) {
		HashMap<String, Integer> hashMap = new HashMap<>();
		hashMap.put("article_id", article_id);
		hashMap.put("user_id", user_id);
		
		if (articleService.deleteArticleLike(hashMap) > 0) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}

		return new ResponseEntity<String>("fail", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
