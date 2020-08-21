package com.ssafy.jara.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.BarterCommentDao;
import com.ssafy.jara.dto.BarterComment;

@Service
public class BarterCommentServiceImpl implements BarterCommentService {
	@Autowired
	BarterCommentDao barterCommentDao;

	@Override
	public int insertBarterComment(BarterComment barterComment) {
		return barterCommentDao.insertBarterComment(barterComment);
	}

	@Override
	public int updateBarterComment(BarterComment barterComment) {
		return barterCommentDao.updateBarterComment(barterComment);
	}

	@Override
	public int deleteBarterComment(int id) {
		return barterCommentDao.deleteBarterComment(id);
	}

	@Override
	public List<BarterComment> selectListBarterComment(int item_id) {
		return barterCommentDao.selectListBarterComment(item_id);
	}

	@Override
	public BarterComment selectBarterComment(int id) {
		return barterCommentDao.selectBarterComment(id);
	}

}
