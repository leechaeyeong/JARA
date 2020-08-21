package com.ssafy.jara.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.ArticleCommentDao;
import com.ssafy.jara.dto.ArticleComment;

@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {

	@Autowired
	ArticleCommentDao articleCommentDao;
	
	@Override
	public int insertArticleComment(ArticleComment articleComment) {
		return articleCommentDao.insertArticleComment(articleComment);
	}

	@Override
	public List<ArticleComment> selectArticleComments(int article_id) {
		return articleCommentDao.selectArticleComments(article_id);
	}

	@Override
	public ArticleComment selectArticleComment(int id) {
		return articleCommentDao.selectArticleComment(id);
	}

	@Override
	public int updateArticleComment(ArticleComment articleComment) {
		return articleCommentDao.updateArticleComment(articleComment);
	}

	@Override
	public int deleteArticleComment(int id) {
		return articleCommentDao.deleteArticleComment(id);
	}

	@Override
	public int updateArticleCommentInvisible(int id) {
		return articleCommentDao.updateArticleCommentInvisible(id);
	}

}
