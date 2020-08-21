package com.ssafy.jara.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.jara.dao.EitherDao;
import com.ssafy.jara.dto.Either;
import com.ssafy.jara.dto.EitherChoice;

@Service
public class EitherServiceImpl implements EitherService {
	@Autowired
	EitherDao eitherDao;

	@Override
	public int insertEither(Either either) {
		return eitherDao.insertEither(either);
	}

	@Override
	public Either selectEither(int id) {
		return eitherDao.selectEither(id);
	}

	@Override
	public int updateEither(int id) {
		return eitherDao.updateEither(id);
	}
	
	@Override
	public int deleteEither(int id) {
		return eitherDao.deleteEither(id);
	}

	@Override
	public List<Either> selectListEither() {
		return eitherDao.selectListEither();
	}

	@Override
	public List<Either> selectPartialListEither(int s_idx, int count) {
		List<Either> allList = eitherDao.selectListEither();
		int allLilstCnt = allList.size();
		
		List<Either> partialList = new ArrayList<Either>();
		if (s_idx + count > allLilstCnt) {
			for (int i = s_idx; i < allLilstCnt; i++) {
				partialList.add(allList.get(i));
			}
		} else {
			for (int i = s_idx; i < s_idx + count; i++) {
				partialList.add(allList.get(i));
			}
		}
		
		return partialList;
	}
	
	@Override
	public int pickEither(EitherChoice eitherChoice) {
		return eitherDao.pickEither(eitherChoice);
	}
	
	@Override
	public List<Integer> selectChoiceAList(int either_id) {
		return eitherDao.selectChoiceAList(either_id);
	}
	
	@Override
	public List<Integer> selectChoiceBList(int either_id) {
		return eitherDao.selectChoiceBList(either_id);
	}

	@Override
	public List<Either> selectListEitherTop3() {
		return eitherDao.selectListEitherTop3();
	}
}
