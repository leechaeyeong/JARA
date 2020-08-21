package com.ssafy.jara.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.TipCommentDao;
import com.ssafy.jara.dto.TipComment;

@Service
public class TipCommentServiceImpl implements TipCommentService {

	@Autowired
	TipCommentDao tipCommentDao;
	
	@Override
	public int insertTipComment(TipComment tipComment) {
		return tipCommentDao.insertTipComment(tipComment);
	}

	@Override
	public List<TipComment> selectTipComments(int tip_id) {
		return tipCommentDao.selectTipComments(tip_id);
	}

	@Override
	public TipComment selectTipComment(int id) {
		return tipCommentDao.selectTipComment(id);
	}

	@Override
	public int updateTipComment(TipComment tipComment) {
		return tipCommentDao.updateTipComment(tipComment);
	}

	@Override
	public int deleteTipComment(int id) {
		return tipCommentDao.deleteTipComment(id);
	}

}
