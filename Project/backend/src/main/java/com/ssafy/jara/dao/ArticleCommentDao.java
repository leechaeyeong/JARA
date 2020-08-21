package com.ssafy.jara.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.ssafy.jara.dto.ArticleComment;

@Mapper
public interface ArticleCommentDao {

	// 게시글 댓글 등록
	public int insertArticleComment(ArticleComment articleComment);

	// 게시글 전체 댓글 조회
	public List<ArticleComment> selectArticleComments(int article_id);

	// 게시글 댓글 조회
	public ArticleComment selectArticleComment(int id);

	// 게시글 댓글 수정
	public int updateArticleComment(ArticleComment articleComment);

	// 게시글 댓글 삭제
	public int deleteArticleComment(int id);

	// 게시글 댓글 숨김
	public int updateArticleCommentInvisible(int id);

}
