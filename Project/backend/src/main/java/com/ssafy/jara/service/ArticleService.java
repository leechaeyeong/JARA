package com.ssafy.jara.service;

import java.util.HashMap;
import java.util.List;

import com.ssafy.jara.dto.Article;

public interface ArticleService {

	// 게시글 등록
	public int insertArticle(Article article);

	// 사용자가 작성한 전체 게시글 조회
	public List<Article> selectListMyArticle(int writer);

	// 사용자가 작성한 전체 게시글 및 사용자가 팔로우하는 다른 사용자의 전체 게시글 조회
	public List<Article> selectListArticle(int writer);

	// 사용자가 작성한 전체 게시글 및 사용자가 팔로우하는 다른 사용자의 전체 게시글 s_idx부터 count만큼 조회
	public List<Article> selectRangeListArticle(HashMap<String, Integer> hashMap);

	// 게시글 조회
	public Article selectArticle(int id);

	// 게시글 수정
	public int updateArticle(Article article);

	// 게시글 삭제
	public int deleteArticle(int id);

	// 게시글 좋아요 등록
	public int insertArticleLike(HashMap<String, Integer> hashMap);

	// 게시글 좋아요 삭제
	public int deleteArticleLike(HashMap<String, Integer> hashMap);

	// 게시글 좋아요 하는 사용자 전체 조회
	public List<Integer> selectArticleLikeAccount(int article_id);

	// 게시글 이미지 경로(firebase) 저장
	public int updateArticleImg(HashMap<String, Object> hashMap);
}
