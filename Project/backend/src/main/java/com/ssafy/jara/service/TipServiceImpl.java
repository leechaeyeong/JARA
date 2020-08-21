package com.ssafy.jara.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.TipDao;
import com.ssafy.jara.dto.Tip;

@Service
public class TipServiceImpl implements TipService {

	@Autowired
	TipDao tipDao;
	
	@Override
	public int insertTip(Tip tip) {
		return tipDao.insertTip(tip);
	}

	@Override
	public List<Tip> selectListTip() {
		return tipDao.selectListTip();
	}

	@Override
	public Tip selectTip(int id) {
		return tipDao.selectTip(id);
	}

	@Override
	public int updateTip(Tip tip) {
		return tipDao.updateTip(tip);
	}

	@Override
	public int deleteTip(int id) {
		return tipDao.deleteTip(id);
	}

	@Override
	public int insertTipLike(HashMap<String, Integer> hashMap) {
		return tipDao.insertTipLike(hashMap);
	}
	
	@Override
	public List<Integer> selectTipLikeAccounts(int tip_id) {
		return tipDao.selectTipLikeAccounts(tip_id);
	};

	@Override
	public int selectTipLike(HashMap<String, Integer> hashMap) {
		return tipDao.selectTipLike(hashMap);
	}

	@Override
	public int deleteTipLike(HashMap<String, Integer> hashMap) {
		return tipDao.deleteTipLike(hashMap);
	}

	@Override
	public int insertTipScrap(HashMap<String, Integer> hashMap) {
		return tipDao.insertTipScrap(hashMap);
	}

	@Override
	public List<Tip> selectListTipUserScrap(int user_id) {
		return tipDao.selectListTipUserScrap(user_id);
	}

	@Override
	public List<Tip> selectListTipTop5() {
		return tipDao.selectListTipTop5();
	}

	@Override
	public int updateTipImg(HashMap<String, Object> hashMap) {
		return tipDao.updateTipImg(hashMap);
	}
	
	@Override
	public List<Integer> selectListTipScrap(int tip_id) {
		return tipDao.selectListTipScrap(tip_id);
	}

}
