package com.ssafy.jara.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.EitherCommentDao;
import com.ssafy.jara.dto.EitherComment;

@Service
public class EitherCommentServiceImpl implements EitherCommentService {
	@Autowired
	EitherCommentDao eitherCommentDao;

	@Override
	public int insertEitherComment(EitherComment eitherComment) {
		return eitherCommentDao.insertEitherComment(eitherComment);
	}

	@Override
	public int updateEitherComment(EitherComment eitherComment) {
		return eitherCommentDao.updateEitherComment(eitherComment);
	}

	@Override
	public int deleteEitherComment(int id) {
		return eitherCommentDao.deleteEitherComment(id);
	}

	@Override
	public List<EitherComment> selectListEitherComment(int either_id) {
		return eitherCommentDao.selectListEitherComment(either_id);
	}
	
	@Override
	public EitherComment selectEitherComment(int id) {
		return eitherCommentDao.selectEitherComment(id);
	}
}
