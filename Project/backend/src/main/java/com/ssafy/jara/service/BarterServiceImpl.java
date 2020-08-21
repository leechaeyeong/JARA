package com.ssafy.jara.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.BarterDao;
import com.ssafy.jara.dto.Barter;

@Service
public class BarterServiceImpl implements BarterService {
	@Autowired
	BarterDao barterDao;

	@Override
	public int insertBarter(Barter barter) {
		return barterDao.insertBarter(barter);
	}

	@Override
	public int updateBarter(Barter barter) {
		return barterDao.updateBarter(barter);
	}
	
	@Override
	public int deleteBarter(int id) {
		return barterDao.deleteBarter(id);
	}

	@Override
	public List<Barter> selectListBarter() {
		return barterDao.selectListBarter();
	}

	@Override
	public Barter selectBarter(int id) {
		return barterDao.selectBarter(id);
	}

	@Override
	public int updateBarterStatus(HashMap<String, Object> hashMap) {
		return barterDao.updateBarterStatus(hashMap);
	}
	
	@Override
	public int updateBarterImgSrc(HashMap<String, Object> hashMap) {
		return barterDao.updateBarterImgSrc(hashMap);
	}
}
